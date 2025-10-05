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

        stage('Build Java Project') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh """
                    docker build -t ${DOCKERHUB_REPO}:${IMAGE_TAG} .
                    docker tag ${DOCKERHUB_REPO}:${IMAGE_TAG} ${DOCKERHUB_REPO}:latest
                    """
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    sh """
                    echo "${DOCKERHUB_CREDENTIALS_PSW}" | docker login -u "${DOCKERHUB_CREDENTIALS_USR}" --password-stdin
                    docker push ${DOCKERHUB_REPO}:${IMAGE_TAG}
                    docker push ${DOCKERHUB_REPO}:latest
                    """
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

