# Persisten K8s Build Container

Jenkins- and Kubernetesfile for a sample build container with Docker enabled and keepalive script.

## Getting Started

If you want to use the [Jenkins Kubernetes Plugin](https://github.com/jenkinsci/kubernetes-plugin) to build your applications with [Jenkis](https://jenkins.io/) and [Kubernetes](https://kubernetes.io/) you need to define a build container in the Jenkins Pipeline Language Groovy.

That container needs to fit at least theese conditions:

* Container should not determine itself (It gets determined by Jenkins JNLP Agent after the build job)

And you might want to achive the following points aswell:

* Get a persisten storage for build dependencies (for example a persisten storage for maven dependencies /root/.m2)
* Mount the Docker Socket in the build Container
* Use your own Docker image from a private registry

The following files gives you an example how to achive this.

* The [Workflowdefinition](Workflow.groovy) is the Jenkins Groovy pipeline which will schedule the creation of the Kubernetes container and executes some sample commands.
* The [BuildAgent](BuildAgent.yaml) contains the Kubernetes Pod definiton used to create the build container.

### Prerequisites

To use the scripts you need at least:

* A running Kubernetes Cluster [Setting up Kubernetes](https://kubernetes.io/docs/setup/pick-right-solution/).
* A [Jenkis Server](https://jenkins.io/) with [Jenkins Kubernetes Plugin](https://github.com/jenkinsci/kubernetes-plugin) installed.

#### Optional

The following conditions are optional and you dont need to use them.

##### PrivateDockerRegistry

In my example I used an Image from a private Docker Registry. If you wont use them just enter the Docker image name without a private registry in front and remove the imagePullSecret parameter from the definition.

##### Azure File as storage driver

I have used the [Microsoft Azure Cloud](https://azure.microsoft.com) and the [Kubernetes AzureFile](https://docs.microsoft.com/en-us/azure/aks/azure-files-volume) Driver to create a persistent Storage.
If you wont use that you have to change the volumes section to other [valid Kubernetes Storage Drivers](https://kubernetes.io/docs/concepts/storage/persistent-volumes/).

### Installing

After setting up the Jenkins Kubernetes Connection you can create a new Pipeline Job and paste the content of [Workflow.groovy](Workflow.groovy) into the Pipeline definition window.

Thats all. The Jenkins will execute the Steps inside of the node(label) on an Kubernetes worker.

```groovy
node (label) {
      .
      .
      .
    }
```

Please notice that this is just an example and you might have to customize it!

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.