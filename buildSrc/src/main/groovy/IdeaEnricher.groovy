import org.gradle.api.Project;

public class IdeaEnricher {

    def static enableGitScm(Project gradleProject, Node project) {
        def vcsDirectoryMappings = project.component.find { it.@name == 'VcsDirectoryMappings' } as Node
        if (vcsDirectoryMappings) {
            project.component.remove vcsDirectoryMappings

        }
        vcsDirectoryMappings = project.appendNode('component', [name: 'VcsDirectoryMappings']);
        def builder = new NodeBuilder();
        def mapping = builder.mapping(directory:'$PROJECT_DIR$', vcs:'Git')
        vcsDirectoryMappings.append mapping
    }


    def static updateBuildOutputFolderForGradle(Project gradleProject, Node project) {
        def projectRootManager = project.component.find { it.@name == 'ProjectRootManager' } as Node
        Node output = projectRootManager.output.find{it.@url == 'file://$PROJECT_DIR$/out'}
        if(output) {
            output.@url = 'file://$PROJECT_DIR$/build/ide'
        }
    }
}