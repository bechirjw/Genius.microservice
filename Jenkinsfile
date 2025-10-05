pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')  // ID stocké dans Jenkins
        DOCKERHUB_REPO = 'jouini926'   // Ton repo DockerHub
        IMAGE_TAG = "${GIT_COMMIT.take(7)}"      // Tag basé sur le commit
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/bechirjw/Genius.microservice.git'
            }
        }

        stage('Verify Maven installation') {
            steps {
                sh '''
                if ! command -v mvn &> /dev/null
                then
                    echo "Maven not found, installing..."
                    sudo apt-get update && sudo apt-get install -y maven
                fi
                mvn -v
                '''
            }
        }

        stage('Build and Package Microservices') {
            steps {
                script {
                    def services = ["config-server", "discovery", "gateway"] // ← adapte selon ton repo

                    for (svc in services) {
                        echo "Building ${svc}..."
                        dir("${svc}") {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    def services = ["config-server", "discovery", "gateway"] 
                    for (svc in services) {
                        echo "Building Docker image for ${svc}..."
                        dir("${svc}") {
                            sh """
                            docker build -t ${DOCKERHUB_REPO}-${svc}:${IMAGE_TAG} .
                            docker tag ${DOCKERHUB_REPO}-${svc}:${IMAGE_TAG} ${DOCKERHUB_REPO}-${svc}:latest
                            """
                        }
                    }
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    sh 'echo "${DOCKERHUB_PASS}" | docker login -u "${DOCKERHUB_USER}" --password-stdin'
                    def services = ["config-server", "discovery", "gateway"] 
                    for (svc in services) {
                        echo "Pushing ${svc} to DockerHub..."
                        sh """
                        docker push ${DOCKERHUB_REPO}-${svc}:${IMAGE_TAG}
                        docker push ${DOCKERHUB_REPO}-${svc}:latest
                        """
                    }
                }
            }
        }
    }
        

    post {
        success {
            echo "✅ Build and push completed successfully!"
        }
        failure {
            echo "❌ Pipeline failed!"
        }
    }
}

