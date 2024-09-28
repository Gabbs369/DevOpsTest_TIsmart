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
        
        stage('Build') {
            steps {
                script {
                    sh 'docker build -t frontend-app .'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Establecer el contexto de Kubernetes usando kubeconfig
                    sh "kubectl --kubeconfig=${KUBE_CONFIG} apply -f frontend-deployment.yaml"
                    sh "kubectl --kubeconfig=${KUBE_CONFIG} apply -f frontend-service.yaml"
                }
            }
        }
    }

    post {
        success {
            echo 'Aplicación desplegada exitosamente en Kubernetes.'
        }
        failure {
            echo 'Hubo un error durante el despliegue.'
        }
    }
}
