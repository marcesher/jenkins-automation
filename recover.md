## Seed Job
>Inspired by ( and stolen from ) https://github.com/sheehan/job-dsl-gradle-example

You can re-create the example seed job via the Rest API Runner (see below) using the pattern `jobs/recover.groovy`.

## REST API Runner

A gradle task is configured that can be used to create/update jobs via the Jenkins REST API, if desired. Normally
a seed job is used to keep jobs in sync with the DSL, but this runner might be useful if you'd rather process the
DSL outside of the Jenkins environment or if you want to create the seed job from a DSL script.

```./gradlew rest -Dpattern=<pattern> -DbaseUrl=<baseUrl> [-Dusername=<username>] [-Dpassword=<password>]```

* `pattern` - ant-style path pattern of files to include
* `baseUrl` - base URL of Jenkins server
* `username` - Jenkins username, if secured
* `password` - Jenkins password or token, if secured

This uses a task named `rest` to execute `jenkins.automation.rest.RestApiScriptRunner`, 
which lives in the `jenkins-automation` repo and is available here since `jenkins-automation` is a dependency.