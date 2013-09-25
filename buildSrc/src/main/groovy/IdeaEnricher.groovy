import org.gradle.api.Project;

public class IdeaEnricher {

    def static updateWebArtifacts(Project gradleProject, Node project) {
        def artifactManager = project.component.find { it.@name == 'ArtifactManager' } as Node
        if (artifactManager) {
            Node artifact = artifactManager.artifact.find { it.@type == 'exploded-war' }
            if (artifact) {
                artifactManager.remove(artifact)
            }
        } else {
            artifactManager = project.appendNode('component', [name: 'ArtifactManager']);
        }
        def builder = new NodeBuilder();
        def artifact = builder.artifact(type: 'exploded-war', name: "${gradleProject.name}/Exploded war") {
            'output-path'("\$PROJECT_DIR\$/build/libs/${gradleProject.name}_exploded.war")
            root(id: 'root') {
                element(id: 'javaee-facet-resources', facet: "${gradleProject.name}/web/Web");
                element(id: 'directory', name: 'WEB-INF') {
                    element(id: 'directory', name: 'classes') {
                        element(id: 'module-output', name: "${gradleProject.name}")
                    }
                    element(id: 'directory', name: 'lib') {
                        gradleProject.configurations.runtime.each {
                            element(id: 'file-copy', path: it)
                        }
                    }
                }
            }
        }
        artifactManager.append artifact

    }

    def static enableAjcCompiler(Project gradleProject, Node project) {
        def compilerConfiguration = project.component.find { it.@name == 'CompilerConfiguration' } as Node
        if (compilerConfiguration) {
            Node defaultCompiler = compilerConfiguration.option.find { it.@name == 'DEFAULT_COMPILER' }
            if (defaultCompiler) {
                compilerConfiguration.remove(defaultCompiler)
            }
        }
        compilerConfiguration.appendNode('option', [name: 'DEFAULT_COMPILER', value: 'ajc']);
        def ajcSettings = project.component.find { it.@name == 'AjcSettings' } as Node
        if (ajcSettings) {
            Node ajcPath = compilerConfiguration.option.find { it.@name == 'ajcPath' }
            if (ajcPath) {
                ajcSettings.remove(ajcPath)
            }
        } else {
            ajcSettings = project.appendNode('component', [name: 'AjcSettings']);
        }
        def path
        gradleProject.configurations.ajc.each {
            path = it
        }
        def builder = new NodeBuilder()
        def ajcPath = builder.option(name: 'ajcPath', value: path)
        ajcSettings.append(ajcPath)
    }

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

    def static updateWebFacet(Project gradleProject, Node module) {
        def facetManager = module.component.find { it.@name == 'FacetManager' } as Node
        if (facetManager) {
            Node webFacet = facetManager.facet.find { it.@type == 'web' }
            if (webFacet) {
                facetManager.remove(webFacet)
            }
        } else {
            facetManager = module.appendNode('component', [name: 'FacetManager']);
        }
        def builder = new NodeBuilder();
        def webFacet = builder.facet(type: "web", name: 'Web') {
            configuration {
                descriptors {
                    deploymentDescriptor(name: 'web.xml', url: 'file://$MODULE_DIR$/src/main/webapp/WEB-INF/web.xml')
                }
                webroots {
                    root(url: 'file://$MODULE_DIR$/src/main/webapp', relative: '/')
                }
                sourceRoots {
                    root(url: 'file://$MODULE_DIR$/src/main/java')
                    root(url: 'file://$MODULE_DIR$/src/main/resources')
                }
            }
        }
        facetManager.append webFacet
    }

    def static updateBuildOutputFolderForGradle(Project gradleProject, Node project) {
        def projectRootManager = project.component.find { it.@name == 'ProjectRootManager' } as Node
        Node output = projectRootManager.output.find{it.@url == 'file://$PROJECT_DIR$/out'}
        if(output) {
            output.@url = 'file://$PROJECT_DIR$/build/ide'
        }
    }
}