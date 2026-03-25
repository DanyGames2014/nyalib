package net.modificationstation.stationapi.gradle;

import groovy.util.Node;
import net.fabricmc.loom.util.GroovyXmlUtil;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubprojectHelpers {


    public static void addModuleDependencies(Project project, String... projectNames) {
        List<Dependency> modules = Arrays.stream(projectNames).map((it) -> project.getDependencies().project(Map.of("path", ":" + it, "configuration", "dev"))).collect(Collectors.toList());
//        Arrays.stream(projectNames).forEach((it) -> project.getDependencies().project(Map.of("path", ":" + it, "configuration", "dev")));

        modules.forEach(dependency -> project.getDependencies().add("implementation", dependency));

        MavenPublication publishing = (MavenPublication) project.getExtensions().getByType(PublishingExtension.class).getPublications().getByName("mavenJava");
        publishing.pom((e) -> e.withXml((f) -> addDependencies(f.asNode(), "implementation", modules)));
    }

    public static void addDependencyXML(Node xml, String scope, Project dependency) {
        Node depsNode = GroovyXmlUtil.getOrCreateNode(xml, "dependencies");

        Node appNode = depsNode.appendNode("dependency");
        appNode.appendNode("groupId", dependency.getGroup());
        appNode.appendNode("artifactId", dependency.getName());
        appNode.appendNode("version", dependency.getVersion());
        appNode.appendNode("scope", scope);
    }

    public static void addDependencies(Node xml, String scope, List<Dependency> dependencies) {
        Node depsNode = GroovyXmlUtil.getOrCreateNode(xml, "dependencies");

        for (Dependency dep : dependencies) {
            Node appNode = depsNode.appendNode("dependency");
            appNode.appendNode("groupId", dep.getGroup());
            appNode.appendNode("artifactId", dep.getName());
            appNode.appendNode("version", dep.getVersion());
            appNode.appendNode("scope", scope);
        }
    }

    public static String getSubprojectVersion(Project project, String ver) {
        return project.getProperties().get("mod_version") + "-" + ver;
    }

}
