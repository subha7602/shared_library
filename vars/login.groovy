def call(Map config = [:]) {
    sh "aws ecr get-login-password --region ${config.region} | docker login --username AWS --password-stdin ${config.repo_uri}"
}