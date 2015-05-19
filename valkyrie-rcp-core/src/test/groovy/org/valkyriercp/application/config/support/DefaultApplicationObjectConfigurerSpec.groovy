/*
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.application.config.support

import org.springframework.context.MessageSource
import org.valkyriercp.command.config.CommandButtonIconInfo
import org.valkyriercp.command.config.CommandButtonLabelInfo
import org.valkyriercp.command.config.CommandIconConfigurable
import org.valkyriercp.command.config.CommandLabelConfigurable
import org.valkyriercp.core.DescriptionConfigurable
import org.valkyriercp.core.LabelConfigurable
import org.valkyriercp.core.Secured
import org.valkyriercp.core.TitleConfigurable
import org.valkyriercp.core.support.LabelInfo
import org.valkyriercp.image.IconSource
import org.valkyriercp.image.ImageSource
import org.valkyriercp.image.config.IconConfigurable
import org.valkyriercp.image.config.ImageConfigurable
import org.valkyriercp.security.SecurityController
import org.valkyriercp.security.SecurityControllerManager
import org.valkyriercp.test.TestIcon
import spock.lang.FailsWith
import spock.lang.Specification

import javax.swing.Icon
import javax.swing.ImageIcon
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage

class DefaultApplicationObjectConfigurerSpec extends Specification {
    def "null object should succeed configuration"() {
        given:
        def configurer = new DefaultApplicationObjectConfigurer()

        expect:
        configurer.configure(null, "bogus")
    }

    @FailsWith(IllegalArgumentException)
    def "object without now should fail configuration"() {
        given:
        def configurer = new DefaultApplicationObjectConfigurer()

        expect:
        configurer.configure("bogus", null)
    }

    def "title configurable with title in messagesource"() {
        given:
        def messageSource = Mock(MessageSource)
        def configurer = new DefaultApplicationObjectConfigurer(messageSource)
        String objectName = "bogus";
        String messageCode = objectName + ".title";
        String message = "bogusMessage";
        messageSource.getMessage(messageCode, null, Locale.getDefault()) >> message
        def titleConfigurable = Mock(TitleConfigurable)

        when:
        configurer.configure(titleConfigurable, objectName)

        then:
        1 * titleConfigurable.setTitle(message)
    }

    def "title configurable without title in messagesource"() {
        given:
        def messageSource = Mock(MessageSource)
        def configurer = new DefaultApplicationObjectConfigurer(messageSource)
        String objectName = "bogus";
        String messageCode = objectName + ".title";
        messageSource.getMessage(messageCode, null, Locale.getDefault()) >> null
        def titleConfigurable = Mock(TitleConfigurable)

        when:
        configurer.configure(titleConfigurable, objectName)

        then:
        0 * titleConfigurable._
    }

    def "description configurable with description in messagesource"() {
        given:
        def messageSource = Mock(MessageSource)
        def configurer = new DefaultApplicationObjectConfigurer(messageSource)
        String objectName = "bogus";
        String messageCode = objectName + ".description";
        String message = "bogusMessage";
        messageSource.getMessage(messageCode, null, Locale.getDefault()) >> message
        def descriptionConfigurable = Mock(DescriptionConfigurable)

        when:
        configurer.configure(descriptionConfigurable, objectName)

        then:
        1 * descriptionConfigurable.setDescription(message)
    }

    def "description configurable without description in messagesource"() {
        given:
        def messageSource = Mock(MessageSource)
        def configurer = new DefaultApplicationObjectConfigurer(messageSource)
        String objectName = "bogusTitleable";
        String messageCode = objectName + ".description";
        messageSource.getMessage(messageCode, null, Locale.getDefault()) >> null
        def descriptionConfigurable = Mock(DescriptionConfigurable)

        when:
        configurer.configure(descriptionConfigurable, objectName)

        then:
        0 * descriptionConfigurable._
    }

    def "label configurable with label in messagesource"() {
        given:
        def messageSource = Mock(MessageSource)
        def configurer = new DefaultApplicationObjectConfigurer(messageSource)
        String objectName = "bogus";
        String messageCode = objectName + ".label";
        String message = "bogusMessage";
        messageSource.getMessage(messageCode, null, Locale.getDefault()) >> message
        def labelConfigurable = Mock(LabelConfigurable)

        when:
        configurer.configure(labelConfigurable, objectName)

        then:
        1 * labelConfigurable.setLabelInfo(LabelInfo.valueOf(message))
    }

    def "label configurable without label in messagesource"() {
        given:
        def messageSource = Mock(MessageSource)
        def configurer = new DefaultApplicationObjectConfigurer(messageSource)
        String objectName = "bogusTitleable";
        String messageCode = objectName + ".label";
        messageSource.getMessage(messageCode, null, Locale.getDefault()) >> null
        def labelConfigurable = Mock(LabelConfigurable)

        when:
        configurer.configure(labelConfigurable, objectName)

        then:
        0 * labelConfigurable._
    }

    def "command label configurable with label in messagesource"() {
        given:
        def messageSource = Mock(MessageSource)
        def configurer = new DefaultApplicationObjectConfigurer(messageSource)
        String objectName = "bogus";
        String messageCode = objectName + ".label";
        String message = "bogusMessage";
        messageSource.getMessage(messageCode, null, Locale.getDefault()) >> message
        def labelConfigurable = Mock(CommandLabelConfigurable)

        when:
        configurer.configure(labelConfigurable, objectName)

        then:
        1 * labelConfigurable.setLabelInfo(CommandButtonLabelInfo.valueOf(message))
    }

    def "command label configurable without label in messagesource"() {
        given:
        def messageSource = Mock(MessageSource)
        def configurer = new DefaultApplicationObjectConfigurer(messageSource)
        def objectName = "bogusTitleable";
        def messageCode = objectName + ".label";
        messageSource.getMessage(messageCode, null, Locale.getDefault()) >> null
        def labelConfigurable = Mock(CommandLabelConfigurable)

        when:
        configurer.configure(labelConfigurable, objectName)

        then:
        0 * labelConfigurable._
    }

    def testConfigureIconConfigurable() {
        given:
        def objectName = "bogusIconConfigurable";
        def iconKey = objectName + ".icon";
        def expectedIcon = new TestIcon(Color.GREEN);
        def iconSource = Mock(IconSource.class);
        def configurable = Mock(IconConfigurable.class);
        iconSource.getIcon(iconKey) >> expectedIcon
        def configurer = new DefaultApplicationObjectConfigurer(null, null, iconSource,
                null);

        when:
        configurer.configure(configurable, objectName)

        then:
        1 * configurable.setIcon(expectedIcon)
    }

    def testConfigureIconConfigurableWithNoIconFound() {
        given:
        def objectName = "bogusIconConfigurable";
        def iconKey = objectName + ".icon";
        def iconSource = Mock(IconSource.class);
        def configurable = Mock(IconConfigurable.class);
        iconSource.getIcon(iconKey) >> null
        def configurer = new DefaultApplicationObjectConfigurer(null, null, iconSource,
                null);

        when:
        configurer.configure(configurable, objectName)

        then:
        0 * configurable.setIcon(_)
    }

    public void testCommandIconConfigurable() {
        given:
        String objectName = "bogusCommandIconConfigurable";
        String iconKey = objectName + ".icon";
        String disabledIconKey = objectName + ".disabledIcon";
        String selectedIconKey = objectName + ".selectedIcon";
        String rolloverIconKey = objectName + ".rolloverIcon";
        String pressedIconKey = objectName + ".pressedIcon";
        String largeIconKey = objectName + ".large.icon";
        String disabledLargeIconKey = objectName + ".large.disabledIcon";
        String selectedLargeIconKey = objectName + ".large.selectedIcon";
        String rolloverLargeIconKey = objectName + ".large.rolloverIcon";
        String pressedLargeIconKey = objectName + ".large.pressedIcon";

        Icon expectedIcon = new ImageIcon();
        Icon expectedSelectedIcon = new ImageIcon();
        Icon expectedRolloverIcon = new ImageIcon();
        Icon expectedDisabledIcon = new ImageIcon();
        Icon expectedPressedIcon = new ImageIcon();

        Icon expectedLargeIcon = new ImageIcon();
        Icon expectedSelectedLargeIcon = new ImageIcon();
        Icon expectedRolloverLargeIcon = new ImageIcon();
        Icon expectedDisabledLargeIcon = new ImageIcon();
        Icon expectedPressedLargeIcon = new ImageIcon();

        def configurable = Mock(CommandIconConfigurable)
        def iconSource = Mock(IconSource);
        def configurer =  new DefaultApplicationObjectConfigurer(null, null, iconSource,
                null)

        when:
        iconSource.getIcon(iconKey) >> expectedIcon
        iconSource.getIcon(disabledIconKey) >> expectedDisabledIcon
        iconSource.getIcon(selectedIconKey) >> expectedSelectedIcon
        iconSource.getIcon(rolloverIconKey) >> expectedRolloverIcon
        iconSource.getIcon(pressedIconKey) >> expectedPressedIcon
        iconSource.getIcon(largeIconKey) >> expectedLargeIcon
        iconSource.getIcon(disabledLargeIconKey) >> expectedDisabledLargeIcon
        iconSource.getIcon(selectedLargeIconKey) >> expectedSelectedLargeIcon
        iconSource.getIcon(rolloverLargeIconKey) >> expectedRolloverLargeIcon
        iconSource.getIcon(pressedLargeIconKey) >> expectedPressedLargeIcon
        configurer.configure(configurable, objectName);
        then:
        1 * configurable.setIconInfo(new CommandButtonIconInfo(expectedIcon, expectedSelectedIcon,
                expectedRolloverIcon, expectedDisabledIcon, expectedPressedIcon));
        1 * configurable.setLargeIconInfo(new CommandButtonIconInfo(expectedLargeIcon,
                expectedSelectedLargeIcon, expectedRolloverLargeIcon, expectedDisabledLargeIcon,
                expectedPressedLargeIcon));

        when:
        configurer.loadOptionalIcons = false
        iconSource.getIcon(iconKey) >> expectedIcon
        iconSource.getIcon(largeIconKey) >> expectedLargeIcon
        configurer.configure(configurable, objectName);
        then:
        1 * configurable.setIconInfo(new CommandButtonIconInfo(expectedIcon));
        1 * configurable.setLargeIconInfo(new CommandButtonIconInfo(expectedLargeIcon));
    }

    def testCommandIconConfigurableWithNoIconFound() {
        given:
        String objectName = "bogusCommandIconConfigurable";
        String iconKey = objectName + ".icon";
        String largeIconKey = objectName + ".large.icon";
        def iconSource = Mock(IconSource.class);
        def configurable = Mock(CommandIconConfigurable.class);
        def configurer = new DefaultApplicationObjectConfigurer(null, null, iconSource,
                null);

        when:
        iconSource.getIcon(iconKey) >> null
        iconSource.getIcon(largeIconKey) >> null
        configurer.configure(configurable, objectName);
        then:
        0 * configurable.setIconInfo(_)
        0 * configurable.setLargeIconInfo(_)
    }

    def testImageConfigurable() {
        given:
        String objectName = "bogusImageConfigurable";
        String iconKey = objectName + ".image";
        Image expectedImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        def imageSource = Mock(ImageSource.class);
        def configurable = Mock(ImageConfigurable.class);
        def configurer = new DefaultApplicationObjectConfigurer(null, imageSource, null,
                null);

        when:
        imageSource.getImage(iconKey) >> expectedImage
        configurer.configure(configurable, objectName)
        then:
        1 * configurable.setImage(expectedImage)
    }

    def testImageConfigurableWithNoImageFound() {
        given:
        String objectName = "bogusImageConfigurable";
        String iconKey = objectName + ".image";
        def imageSource = Mock(ImageSource.class);
        def configurable = Mock(ImageConfigurable.class);
        def configurer = new DefaultApplicationObjectConfigurer(null, imageSource, null,
                null);

        when:
        imageSource.getImage(iconKey) >> null
        configurer.configure(configurable, objectName)
        then:
        0 * configurable.setImage(_)
    }

    def testSecurityControllable() {
        given:
        String objectName = "bogusSecurityControllable";
        String securityControllerId = "bogusSecurityControllerId";
        def controllerManager = Mock(SecurityControllerManager.class);
        def controllable = Mock(Secured.class);
        def controller = Mock(SecurityController.class);
        def configurer = new DefaultApplicationObjectConfigurer(null, null, null,
                controllerManager);

        when:
        controllable.getSecurityControllerId() >> securityControllerId
        controllerManager.getSecurityController(securityControllerId) >> null
        configurer.configure(controllable, objectName);
        then:
        0 * controller.addControlledObject(controllable)

        when:
        controllable.getSecurityControllerId() >> securityControllerId
        controllerManager.getSecurityController(securityControllerId) >> controller
        configurer.configure(controllable, objectName);
        then:
        1 * controller.addControlledObject(controllable)
    }
}