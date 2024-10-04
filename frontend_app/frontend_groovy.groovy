pipeline {
    agent any

    environment {
        PROJECT_ID = 'devops-project-437014'
        GCR_HOSTNAME = 'gcr.io'
        GKE_CLUSTER = 'devopstest'
        GKE_ZONE = 'us-central1-c'
        IMAGE_NAME = "gabbs3/frontend-app"
        BUILD_NUMBER = "0.1"
        DEPLOYMENT_NAME = 'frontend-app'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/Gabbs369/DevOpsTest_TIsmart'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("gabbs3/${IMAGE_NAME}:$BUILD_NUMBER")
                }
            }
        }

        stage('Deploy to GKE') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'gke-credentials', variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
                        sh """
                        gcloud auth activate-service-account --key-file=${GOOGLE_APPLICATION_CREDENTIALS}
                        gcloud container clusters get-credentials ${GKE_CLUSTER} --zone ${GKE_ZONE} --project ${PROJECT_ID}
                        
                        # Update deployment with new image
                        kubectl set image deployment/${DEPLOYMENT_NAME} ${DEPLOYMENT_NAME}=${IMAGE_NAME}:$BUILD_NUMBER --record

                        # Apply service and deployment if necessary
                        kubectl apply -f svc.yml
                        kubectl apply -f frontend-deployment.yml
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
