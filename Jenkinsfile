pipeline {
    agent any
    stages {
        stage('Unit Testing') {
            steps {
                sh 'mvn test'
            }
        }
        stage('SonarQube analysis') {
            steps {
                sh 'mvn clean package sonar:sonar -Dsonar.projectKey=Users-Spring-Boot -Dsonar.host.url=http://localhost:9000 -Dsonar.login=848b05cbc78baac42f4b48bb379f4eabcd8f77ee -Dsonar.qualitygate.wait=true'
            }
        }
    }
}