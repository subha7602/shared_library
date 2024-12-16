def call(Map config = [:]) {
    def repoUri = config.repo_uri
    def tag = config.tag ?: 'latest'
 
    sh "echo ${repoUri}"
    sh "echo ${tag}"
    sh "docker images"
    sh "docker push ${repoUri}:${tag}"
 
}