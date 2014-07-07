package org.valkyriercp

import groovy.json.JsonBuilder
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.ResourceLoader

/**
 * Created by lievendoclo on 07/07/14.
 */
class PropertiesToJson {
    public static void main(String[] args) {
        Properties props = new Properties();
        ResourceLoader loader = new DefaultResourceLoader()
        props.load(loader.getResource("classpath:/org/valkyriercp/messages/default.properties").inputStream)

        JsonBuilder builder = new JsonBuilder(props.sort { it.key });

        println builder.toPrettyString()
    }
}
