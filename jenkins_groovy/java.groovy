pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'mi-imagen-java' // Cambia esto según tus necesidades
        KUBE_CONFIG = '/root/.kube/config' // Ruta donde se encuentra el kubeconfig
        K8S_DEPLOYMENT_NAME = 'mi-aplicacion-java'
        K8S_NAMESPACE = 'jenkins'
    }

    stages {
        stage('Clonar Repositorio') {
            steps {
                git 'https://github.com/Pruthvi360/deveops-course.git'
            }
        }
        
        stage('Construir Aplicación') {
            steps {
                script {
                    sh 'mvn clean package'
                }
            }
        }
        
        stage('Construir Imagen Docker') {
            steps {
                script {
                    // Construir la imagen Docker
                    sh "docker build -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }
        
        stage('Desplegar en Kubernetes') {
            steps {
                script {
                    // Desplegar la aplicación en Kubernetes usando kubectl
                    sh """
                    kubectl --kubeconfig=${KUBE_CONFIG} set image deployment/${K8S_DEPLOYMENT_NAME} ${K8S_DEPLOYMENT_NAME}=${DOCKER_IMAGE}:latest --namespace=${K8S_NAMESPACE} || \
                    kubectl --kubeconfig=${KUBE_CONFIG} create deployment ${K8S_DEPLOYMENT_NAME} --image=${DOCKER_IMAGE}:latest --namespace=${K8S_NAMESPACE}
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo 'Despliegue exitoso!'
        }
        failure {
            echo 'Falló el despliegue.'
        }
    }
}
