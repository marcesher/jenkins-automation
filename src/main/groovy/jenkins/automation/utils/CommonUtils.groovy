package jenkins.automation.utils

/**
 * Utils class when most reused common properties should live.
 *  Adds a minimum base functionality required -build claiming, notifications and log.
 * @param context delegate passed in context
 */
class CommonUtils {

    /**
     * Adds bare minimum defaults
     */

    static void addDefaults(context) {
        context.with {
            wrappers {
                colorizeOutput()
            }
            logRotator {
                numToKeep(30)
            }
            publishers {
                allowBrokenBuildClaiming()
            }
            configure { Node project ->
                project / 'properties' / 'com.sonyericsson.jenkins.plugins.bfa.model.ScannerJobProperty'(plugin: "build-failure-analyzer") {
                    doNotScan 'false'
                }
            }
        }
    }

    /** Utility function to add extended email
     *
     * @param List emails List of email string to make it seamlessly compatible with builders
     * @param triggers List<String> triggers E.g Failure, Fixed etc...
     * @param sendToDevelopers Default false,
     * @param sendToRequester Default true,
     * @param includeCulprits Default false,
     * @param sendToRecipientList Default true
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addExtendedEmail(context, List<String> emails, List<String> triggers = ["Failure", "Unstable", "Fixed"], sendToDevelopers = false,  sendToRequester = true, includeCulprits = false, sendToRecipientList = true) {
        addExtendedEmail(context, emails.join(","), triggers, sendToDevelopers, sendToRequester, includeCulprits, sendToRecipientList)
    }

    /**
     * Utility function to add extended email
     * @param String emails Comma separated string of emails
     * @param triggers List<String> triggers E.g Failure, Fixed etc...
     * @param sendToDevelopers Default false,
     * @param sendToRequester Default true,
     * @param includeCulprits Default false,
     * @param sendToRecipientList Default true
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addExtendedEmail(context, String emails, List<String> triggers = ["Failure", "Unstable", "Fixed"], sendToDevelopers = false, sendToRequester = true, includeCulprits = false, sendToRecipientList = true) {
        context.with {
            extendedEmail(emails) {
                triggers.each {
                    trigger(triggerName: it,
                            sendToDevelopers: sendToDevelopers, sendToRequester: sendToRequester, includeCulprits: includeCulprits, sendToRecipientList: sendToRecipientList)
                }
            }

        }
    }

    /**
     * Utility function to add injectGlobalPasswords
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addInjectGlobalPasswords(context) {
        context.with {
            configure {
                it / buildWrappers / EnvInjectPasswordWrapper {
                    injectGlobalPasswords(true)
                    maskPasswordParameters(true)
                }
            }
        }
    }

    /**
     * Utility function to add log parser publisher
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addLogParserPublisher(context, rulesPath="/var/lib/jenkins/shell_parse_rules.txt") {
        context.with {
            configure {
                it / publishers  << 'hudson.plugins.logparser.LogParserPublisher' {
                    unstableOnWarning true
                    failBuildOnError true
                    parsingRulesPath rulesPath
                }
            }
        }
    }

    /**
     * Utility to add a performance publisher block, for use in performance tests
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addPerformancePublisher(context, String reportPattern="**/results/*.jtl", String unstableResponseTimeThreshold="", int failedThresholdPositive, int failedThresholdNegative, int unstableThresholdPositive, int unstableThresholdNegative) {
        context.with {
            configure {
                it / publishers << 'hudson.plugins.performance.PerformancePublisher' {
                    errorFailedThreshold 2
                    errorUnstableThreshold 1
                    errorUnstableResponseTimeThreshold unstableResponseTimeThreshold
                    relativeFailedThresholdPositive failedThresholdPositive
                    relativeFailedThresholdNegative failedThresholdNegative
                    relativeUnstableThresholdPositive unstableThresholdPositive
                    relativeUnstableThresholdNegative unstableThresholdNegative
                    modeRelativeThresholds false
                    configType 'ART'
                    modeOfThreshold true
                    compareBuildPrevious true
                    modePerformancePerTestCase true
                    modeThroughput true
                    parsers {
                        'hudson.plugins.performance.JMeterParser' {
                            glob reportPattern
                        }
                    }
                }
            }
        }
    }

    /**
     * Common string for creating and activating a python 2.7 virtualenv in a shell block
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */
    static String python27Virtualenv = """
        if [ -d ".env" ]; then
          echo "**> virtualenv exists"
        else
          echo "**> creating virtualenv"
          virtualenv -p /usr/local/bin/python2.7 .env
        fi

        . .env/bin/activate

        if [ -f "requirements.txt" ]; then
            pip install -r requirements.txt
        fi
        """.stripIndent()
}