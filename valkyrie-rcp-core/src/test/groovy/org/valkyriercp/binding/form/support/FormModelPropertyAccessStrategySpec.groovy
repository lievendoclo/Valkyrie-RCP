package org.valkyriercp.binding.form.support
import org.valkyriercp.test.TestBean
import spock.lang.Specification
/**
 * Created by lievendoclo on 08/07/14.
 */
class FormModelPropertyAccessStrategySpec extends Specification {
    def testReadOnlyPropertyAccess() {
        when:
        def model = new TestAbstractFormModel(new TestBean());
        def propertyAccessStrategy = model.getPropertyAccessStrategy();
        def metaDataAccessStrategy = propertyAccessStrategy.getMetadataAccessStrategy();
        then:
        !metaDataAccessStrategy.isWriteable("readOnly")
        metaDataAccessStrategy.isReadable("readOnly")
    }
}