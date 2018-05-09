def label = "BuildImage-${UUID.randomUUID().toString()}"
podTemplate(label: label, yaml: '''
apiVersion: v1
kind: Pod
metadata:
  name: example-build-container
  labels:
    app: example-build-container
spec:
    containers: # Definition of the container inside a kubernetes pod
    - name: dockermavenbuild # Name of the Container
      image: myownregistry.registrydomain.com/mymaven:latest # Image of the Container
      imagePullPolicy: Always # Always pull Build Container because there might be updates
      command: [ "/bin/bash" ] # Execute Bash
      args: ["-c", 'run=1; while true; do echo "This is the $run run and I am still alive!"; run=$((run+1)); sleep 3; done'] # A simple keepalive script to prevent the container from beeing closed and restarted.
      securityContext:
        runAsUser: 0 # Container will be run as user root (otherwise change to a diffrent user id)
      volumeMounts: # Defines Mount points for the container
          - name: m2-data # Mounts the persistent Maven Storage
            mountPath: /root/.m2 # mount Endpoint in the Container
          - name: docker-socket # Mounts the Docker Socket inside of the Container
            mountPath: /var/run/docker.sock 
    imagePullSecrets:
    - name: mycontainerregistry-secret # The ImagePull Secret - Needs to be configured in Kubernetes otherwise it wont work
    volumes: # Defines Volumes
        - name: m2-data # Defines the volume for persisten maven Storage
          azureFile: # The Volume uses the azurefile drive which will connect to an azure File Storage for persistent Volume Storage
            secretName: azure-storage-account-secret # This secret contains all information Kubernetes needs to connect to the Azure Storage Account - This must alreade exist in the Kubernetes Namespace
            shareName: maven-volume # Name of the File share in the Azure Storage Account - The File share must already exist in the Azure
            readOnly: false # Mounts the Volume as Read/Write
        - name: docker-socket # Creates a Share wich enable Docker Socket in the Container
          hostPath:
            path: /var/run/docker.sock # Path to Docker Socket
            type: Socket # Type Socket
'''
) 
{
    node (label) {
      container('buildimage') {
        sh "hostname"
        sh "touch /root/.m2/helloworld"
        sh "docker ps"
      }
    }
}
