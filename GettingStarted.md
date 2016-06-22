## Here is how to get started with JAC@CFPB

This will take some effort on your part, but we are hoping to make it easy for you.

Related repositories:

- https://github.com/jenkinsci/job-dsl-plugin: `job-dsl-plugin` is the heart of what we call `jenkins-as-code`
  - Once you start to get familiar with job-dsl, the [API Viewer](https://jenkinsci.github.io/job-dsl-plugin/) is your best friend
- https://github.com/cfpb/jenkins-automation: Our additional abstractions for simplifying working with job-dsl; these include `builders` for common job types and `utils` for helper decision-making and additional common job configuration
- https://github.com/cfpb/jenkins-as-code-starter-project: An incredibly useful **starter repo** for your project's jenkins jobs
- See the other repos in `config/registered-projects-config.groovy` for all kinds of examples.

## Jenkins-as-code workflows

> I want to create a new job. Should I do that in the Jenkins UI and then port it to JAC? Or do it all in JAC? Why? Are there times when one approach is preferable?

**Expected outcome**: all jobs configured with `job-dsl` and kept in GitHub

We aren't prescriptive on how you get there, though we have a  strong bias toward sticking in the job-dsl edit/save/commit/push loop 
to guarantee that you don't make changes in the Jenkins UI that get trampled by eventual seed job runs. It also keeps the feedback loop tight alerting 
of unforseen issues.
Here are some options.

1. Start with job-dsl and do an edit/save/commit/push/re-run-seed-job cycle
1. Start with job-dsl, fiddle to get it working right in the jenkins UI, and then bring all changes back into job-dsl
1. Start in jenkins UI, get it configured, and then bring all changes into job-dsl

### Example, sticking entirely with job-dsl

**Goal**: You want to create a new job that pulls something from github and runs an execute shell command


**Getting started**:

- You have an existing jenkins-automation repo for your project, or you create one with the `starter-project` linked above
- You have an existing, or add a new, `jobs.groovy` file (or some other name) into your project
- You have registered your project with the automation registration, described above, so that you have a seed job that runs when you commit changes (or you run it manually in jenkins UI)

**Workflow**:

Then:

- You add your new job into your .groovy file, save it, git commit, and git push
- Your seed job runs (or you run it) and assuming everything goes well, you go check out your new job in the Jenkins UI
- You continue to iterate on that job in job-dsl (rather than fiddling with it in the Jenkins UI)
  - you edit in your editor and save changes
  - you git commit and git push
  - you run the seed job
  - you inspect your new job, running it as necessary till it's working
  - **repeat till finished**
  


### Example, starting with job-dsl, tweaking in Jenkins UI, and finishing with job-dsl

**Goal**: You want to create a new job that pulls something from github and runs an execute shell command

Same **Getting started** as in above example

**Workflow**:

Then:

- You add your new job into your .groovy file, save it, git commit, and git push
- Your seed job runs (or you run it) and assuming everything goes well, you go check out your new job in the Jenkins UI
- You now fiddle with your job in the Jenkins UI, probably tweaking the `execute shell` block
- You save and run your job till it's working as expected
  - **repeat till finished**
- You bring those changes back into job-dsl
- You save, git commit, git push
- Run your seed job again, and run your job again in Jenkins to confirm it's working as expected
