import jenkins.automation.utils.ScmUtils

def reposToInclude = [
        [name: "automation", url: "https://github.com/cfpb/jenkins-automation", sub_directory: "automation"],
        [name: "automation-registration", url: "YOUR INTERNAL CONFIG REPO"]

]
listView("Automation (Jenkins)") {
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/(?i)(JAC.*|.*-seed-job)/)
    }
}
job("JAC Automation Registration Seed Job") {

    logRotator {
        daysToKeep(90)
    }

    multiscm {
        ScmUtils.project_repos(delegate, reposToInclude, false)
    }

    triggers {
        scm "H/5 * * * *"
    }

    configure { project ->
        project / publishers << "hudson.plugins.logparser.LogParserPublisher" {
            unstableOnWarning(true)
            failBuildOnError(false)
            useProjectRule(true)
            projectRulePath("automation/log-parser-rules.txt")
        }
    }

    steps {
        dsl {
            external "jobs/jenkins_automation_registration_seed.groovy"
            additionalClasspath "automation/src/main/groovy" + "\r\n" + "src/main/groovy"
            removeAction("DISABLE")
            removeViewAction("DELETE")
        }
    }
    publishers {
        extendedEmail {
            recipientList("YOUR MOTHER SEED JOB RECIPIENT LIST")
            triggers {
                failure {
                    sendTo {
                        recipientList()
                    }
                }
                fixed {
                    sendTo {
                        recipientList()
                    }
                }
            }
        }
    }
}
