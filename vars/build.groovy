def call(Map config = [:]) {   
    // Validate required parameters
    if (!config.repoName || !config.repo_uri) {
        error "Missing required parameters: repoName and repo_uri"
    }
    
    // Set default tag if not provided
    def tag = config.tag ?: 'latest'
    def repoName = config.repoName
    def repoUri = config.repo_uri

    try {
        // Fetch Dockerfile and index.html from library resources
        def dockerfileContent = libraryResource 'Dockerfile'
        writeFile file: 'Dockerfile', text: dockerfileContent
        
        def webfileContent = libraryResource 'index.html'
        writeFile file: 'index.html', text: webfileContent

        // Debugging: Check workspace state
        echo "Workspace contents before Docker build:"
        sh "pwd"
        sh "ls -al"

        // Build Docker image
        echo "Building Docker image ${repoName}:${tag}..."
        docker.build("${repoName}:${tag}", "-f Dockerfile .")

        // Tag Docker image with the repository URI
        echo "Tagging Docker image: ${repoUri}:${tag}"
        sh "docker tag ${repoName}:${tag} ${repoUri}:${tag}"

        // List Docker images for verification
        echo "Docker images available after build:"
        sh "docker images"
    } catch (Exception e) {
        error "Error during Docker build process: ${e.message}"
    } finally {
        // Clean up workspace (optional)
        echo "Cleaning up temporary files..."
        sh "rm -f Dockerfile index.html"
    }
}
