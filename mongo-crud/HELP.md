# Read Me First

The following was discovered as part of building this project:

- The original package name 'com.jai.mongo-crud' is invalid and this project uses 'com.jai.mongo_crud' instead.

# Getting Started

K8S generic template

```
apiVersion: <which-k8s-api>
kind: <what-type-of-thing>
metadata:
    name: <give-it-a-name>
spec:
    <the-actual-config>
```

k8s commands to generate the deployment and service yaml files:

```
kubectl create deployment mongo --image=mongo:latest --dry-run=client -o yaml > mongo-deployment.yaml
kubectl create service clusterip mongo --tcp=27017:27017 --dry-run=client -o yaml > mongo-service.yaml
kubectl create deployment crud-app --image=jjayaraman/mongo-crud:latest --dry-run=client -o yaml > app-deployment.yaml
kubectl create service loadbalancer crud-app --tcp=8080:8080 --dry-run=client -o yaml > app-service.yaml
```

copy all the above 4 outputs files into single file called deployment.yaml for convenience

```
kubectly apply -f deployment.yaml
kubectly delete -f deployment.yaml
```

Helm deployments

cd helm/mongo-crud-sb

helm install mongo-crud-sb .

```
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services mongo-crud-sb)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT
```

### Reference Documentation

For further reference, please consider the following sections:

- [Official Gradle documentation](https://docs.gradle.org)
- [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.6/gradle-plugin)
- [Create an OCI image](https://docs.spring.io/spring-boot/3.5.6/gradle-plugin/packaging-oci-image.html)
- [Spring Data MongoDB](https://docs.spring.io/spring-boot/3.5.6/reference/data/nosql.html#data.nosql.mongodb)

### Guides

The following guides illustrate how to use some features concretely:

- [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)

### Additional Links

These additional references should also help you:

- [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
