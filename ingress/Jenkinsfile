pipeline {
    agent any

    environment {
        KUBE_CONFIG = '/root/.kube/config'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Gabbs369/DevOpsTest_TIsmart'
            }
        }

        stage('Deploy Ingress to Kubernetes') {
            steps {
                script {
                    sh "kubectl --kubeconfig=${KUBE_CONFIG} apply -f ingress.yaml"
                }
            }
        }
    }

    post {
        success {
            echo 'Ingress desplegado'
        }
        failure {
            echo 'Hubo un error'
        }
    }
}
