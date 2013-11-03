package org.valkyriercp.application.config.support;

import junit.framework.Assert;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.valkyriercp.command.config.CommandButtonIconInfo;
import org.valkyriercp.command.config.CommandButtonLabelInfo;
import org.valkyriercp.command.config.CommandIconConfigurable;
import org.valkyriercp.command.config.CommandLabelConfigurable;
import org.valkyriercp.core.DescriptionConfigurable;
import org.valkyriercp.core.LabelConfigurable;
import org.valkyriercp.core.Secured;
import org.valkyriercp.core.TitleConfigurable;
import org.valkyriercp.core.support.LabelInfo;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.image.config.IconConfigurable;
import org.valkyriercp.image.config.ImageConfigurable;
import org.valkyriercp.security.SecurityController;
import org.valkyriercp.security.SecurityControllerManager;
import org.valkyriercp.test.TestIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Locale;

import static org.mockito.Mockito.*;

/**
 * Provides a suite of unit tests for the
 * {@link DefaultApplicationObjectConfigurer} class.
 *
 * @author Kevin Stembridge
 * @since 0.3
 *
 */
public class DefaultApplicationObjectConfigurerTests {

	/**
	 * Creates a new {@code DefaultApplicationObjectConfigurerTests}.
	 */
	public DefaultApplicationObjectConfigurerTests() {
		super();
	}

	/**
	 * Confirms that an IllegalArgumentException is thrown if the object to be
	 * configured is not null but the object name is null.
	 */
	public void testForNullInputToConfigureMethod() {

		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer();

		// should succeed
		configurer.configure(null, "bogus");

		try {
			configurer.configure("bogus", null);
			Assert.fail("Should have thrown an IllegalArgumentException for null object name");
		}
		catch (IllegalArgumentException e) {
			// do nothing, test succeeded
		}

	}

	/**
	 * Confirms that a {@link TitleConfigurable} object will have its title set
	 * correctly, as retrieved from a MessageSource using the key
	 * 'beanName.title'.
	 */
	public void testConfigureTitleConfigurable() {
		MessageSource messageSource = (MessageSource) EasyMock.createMock(MessageSource.class);

		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(messageSource);

		String objectName = "bogusTitleable";
		String messageCode = objectName + ".title";
		String message = "bogusTitle";

		EasyMock.expect(messageSource.getMessage(messageCode, null, Locale.getDefault())).andReturn(message);

		TitleConfigurable configurable = (TitleConfigurable) EasyMock.createMock(TitleConfigurable.class);
		configurable.setTitle(message);

		EasyMock.replay(messageSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(messageSource);
		EasyMock.verify(configurable);
	}

	@Test
    public void testConfigureTitleConfigurableWithNoTitleFound() {
		MessageSource messageSource = (MessageSource) mock(MessageSource.class);
		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(messageSource);
		String objectName = "bogusTitleable";
		String messageCode = objectName + ".title";
		when(messageSource.getMessage(messageCode, null, Locale.getDefault())).thenReturn(null);
		TitleConfigurable configurable = (TitleConfigurable) mock(TitleConfigurable.class);
		configurer.configure(configurable, objectName);
        verifyZeroInteractions(configurable);
	}

	/**
	 * Confirms that a {@link DescriptionConfigurable} object will have its
	 * description and caption set correctly, as retrieved from a MessageSource
	 * using the key 'beanName.description' and 'beanName.caption' respectively.
	 */
	@Test
    public void testDescriptionConfigurable() {
		MessageSource messageSource = (MessageSource) EasyMock.createMock(MessageSource.class);

		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(messageSource);

		String objectName = "bogusDescriptionConfigurable";
		String descriptionCode = objectName + ".description";
		String captionCode = objectName + ".caption";
		String description = "bogusDescription";
		String caption = "bogusCaption";

		EasyMock.expect(messageSource.getMessage(descriptionCode, null, Locale.getDefault())).andReturn(description);
		EasyMock.expect(messageSource.getMessage(captionCode, null, Locale.getDefault())).andReturn(caption);

		DescriptionConfigurable configurable = (DescriptionConfigurable) EasyMock
				.createMock(DescriptionConfigurable.class);
		configurable.setDescription(description);
		configurable.setCaption(caption);

		EasyMock.replay(messageSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(messageSource);
		EasyMock.verify(configurable);
	}

	@Test
    public void testDescriptionConfigurableWithNoDescriptionFound() {
		MessageSource messageSource = (MessageSource) EasyMock.createMock(MessageSource.class);

		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(messageSource);

		String objectName = "bogusDescriptionConfigurable";
		String descriptionCode = objectName + ".description";
		String captionCode = objectName + ".caption";

		EasyMock.expect(messageSource.getMessage(descriptionCode, null, Locale.getDefault())).andReturn(null);
		EasyMock.expect(messageSource.getMessage(captionCode, null, Locale.getDefault())).andReturn(null);

		DescriptionConfigurable configurable = (DescriptionConfigurable) EasyMock
				.createMock(DescriptionConfigurable.class);

		EasyMock.replay(messageSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(messageSource);
		EasyMock.verify(configurable);
	}

	/**
	 * Confirms that a {@link LabelConfigurable} object will have its label set
	 * correctly, as retrieved from a MessageSource using the key
	 * 'beanName.label'.
	 */
	@Test
    public void testLabelConfigurable() {
		MessageSource messageSource = (MessageSource) EasyMock.createMock(MessageSource.class);

		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(messageSource);

		String objectName = "bogusLabelable";
		String messageCode = objectName + ".label";
		String message = "bogusLabelInfo";
		LabelInfo expectedLabelInfo = LabelInfo.valueOf(message);

		EasyMock.expect(messageSource.getMessage(messageCode, null, Locale.getDefault())).andReturn(message);

		LabelConfigurable configurable = (LabelConfigurable) EasyMock.createMock(LabelConfigurable.class);
		configurable.setLabelInfo(expectedLabelInfo);

		EasyMock.replay(messageSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(messageSource);
		EasyMock.verify(configurable);

	}

	@Test
    public void testLabelConfigurableWithNoLabelFound() {
		MessageSource messageSource = (MessageSource) EasyMock.createMock(MessageSource.class);

		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(messageSource);

		String objectName = "bogusLabelable";
		String messageCode = objectName + ".label";

		EasyMock.expect(messageSource.getMessage(messageCode, null, Locale.getDefault())).andReturn(null);

		LabelConfigurable configurable = (LabelConfigurable) EasyMock.createMock(LabelConfigurable.class);

		EasyMock.replay(messageSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(messageSource);
		EasyMock.verify(configurable);
	}

	/**
	 * Confirms that a {@link CommandLabelConfigurable} object will have its
	 * label set correctly, as retrieved from a MessageSource using the key
	 * 'beanName.label'.
	 */
	@Test
    public void testCommandLabelConfigurable() {
		MessageSource messageSource = (MessageSource) EasyMock.createMock(MessageSource.class);

		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(messageSource);

		String objectName = "bogusLabelable";
		String messageCode = objectName + ".label";
		String message = "bogusLabelInfo";
		CommandButtonLabelInfo expectedLabelInfo = CommandButtonLabelInfo.valueOf(message);

		EasyMock.expect(messageSource.getMessage(messageCode, null, Locale.getDefault())).andReturn(message);

		CommandLabelConfigurable configurable = (CommandLabelConfigurable) EasyMock
				.createMock(CommandLabelConfigurable.class);
		configurable.setLabelInfo(expectedLabelInfo);

		EasyMock.replay(messageSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(messageSource);
		EasyMock.verify(configurable);

	}

	@Test
    public void testCommandLabelConfigurableWithNoLabelFound() {
		MessageSource messageSource = (MessageSource) EasyMock.createMock(MessageSource.class);

		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(messageSource);

		String objectName = "bogusLabelable";
		String messageCode = objectName + ".label";

		EasyMock.expect(messageSource.getMessage(messageCode, null, Locale.getDefault())).andReturn(null);

		CommandLabelConfigurable configurable = (CommandLabelConfigurable) EasyMock
				.createMock(CommandLabelConfigurable.class);

		EasyMock.replay(messageSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(messageSource);
		EasyMock.verify(configurable);

	}

	/**
	 * Confirms that a {@link IconConfigurable} object will have its icon set
	 * correctly, if the icon source can find it using the key
	 * 'objectName.icon'.
	 */
	@Test
    public void testConfigureIconConfigurable() {
		String objectName = "bogusIconConfigurable";
		String iconKey = objectName + ".icon";
		Icon expectedIcon = new TestIcon(Color.GREEN);

		// Create the required mock objects
		IconSource iconSource = (IconSource) EasyMock.createMock(IconSource.class);
		IconConfigurable configurable = (IconConfigurable) EasyMock.createMock(IconConfigurable.class);

		// Create the configurer with the mock icon source
		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(null, null, iconSource,
				null);

		// Set the expectations for the mock objects. For the first run, we
		// don't want the icon
		// source to be able to find the icon, so setIcon should be called with
		// a null value.
		EasyMock.expect(iconSource.getIcon(iconKey)).andReturn(expectedIcon);
		configurable.setIcon(expectedIcon);

		EasyMock.replay(iconSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(iconSource);
		EasyMock.verify(configurable);
	}

	@Test
    public void testConfigureIconConfigurableWithNoIconFound() {
		String objectName = "undefinedIconConfigurable";
		String iconKey = objectName + ".icon";

		// Create the required mock objects
		IconSource iconSource = (IconSource) EasyMock.createMock(IconSource.class);
		IconConfigurable configurable = (IconConfigurable) EasyMock.createMock(IconConfigurable.class);

		// Create the configurer with the mock icon source
		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(null, null, iconSource,
				null);

		// Set the expectations for the mock objects. For the first run, we
		// don't want the icon
		// source to be able to find the icon, so setIcon should be called with
		// a null value.
		EasyMock.expect(iconSource.getIcon(iconKey)).andReturn(null);

		EasyMock.replay(iconSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(iconSource);
		EasyMock.verify(configurable);
	}

	/**
	 * Confirms that a {@link CommandIconConfigurable} object will have its
	 * icons set correctly.
	 */
	@Test
    public void testCommandIconConfigurable() {

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

		CommandButtonIconInfo expectedIconInfo = new CommandButtonIconInfo(expectedIcon, expectedSelectedIcon,
				expectedRolloverIcon, expectedDisabledIcon, expectedPressedIcon);

		CommandButtonIconInfo expectedLargeIconInfo = new CommandButtonIconInfo(expectedLargeIcon,
				expectedSelectedLargeIcon, expectedRolloverLargeIcon, expectedDisabledLargeIcon,
				expectedPressedLargeIcon);

		// Create the required mock objects
		IconSource iconSource = (IconSource) EasyMock.createMock(IconSource.class);
		CommandIconConfigurable configurable = (CommandIconConfigurable) EasyMock
				.createMock(CommandIconConfigurable.class);

		// Create the configurer with the mock icon source
		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(null, null, iconSource,
				null);

		EasyMock.expect(iconSource.getIcon(iconKey)).andReturn(expectedIcon);
		EasyMock.expect(iconSource.getIcon(disabledIconKey)).andReturn(expectedDisabledIcon);
		EasyMock.expect(iconSource.getIcon(selectedIconKey)).andReturn(expectedSelectedIcon);
		EasyMock.expect(iconSource.getIcon(rolloverIconKey)).andReturn(expectedRolloverIcon);
		EasyMock.expect(iconSource.getIcon(pressedIconKey)).andReturn(expectedPressedIcon);
		EasyMock.expect(iconSource.getIcon(largeIconKey)).andReturn(expectedLargeIcon);
		EasyMock.expect(iconSource.getIcon(disabledLargeIconKey)).andReturn(expectedDisabledLargeIcon);
		EasyMock.expect(iconSource.getIcon(selectedLargeIconKey)).andReturn(expectedSelectedLargeIcon);
		EasyMock.expect(iconSource.getIcon(rolloverLargeIconKey)).andReturn(expectedRolloverLargeIcon);
		EasyMock.expect(iconSource.getIcon(pressedLargeIconKey)).andReturn(expectedPressedLargeIcon);
		configurable.setIconInfo(expectedIconInfo);
		configurable.setLargeIconInfo(expectedLargeIconInfo);

		EasyMock.replay(iconSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(iconSource);
		EasyMock.verify(configurable);

		// Reset the mock objects for the next test
		EasyMock.reset(iconSource);
		EasyMock.reset(configurable);

		// Set the expectations. This time the loadOptionalIcons will be set to
		// false.
		configurer.setLoadOptionalIcons(false);
		expectedIconInfo = new CommandButtonIconInfo(expectedIcon);
		expectedLargeIconInfo = new CommandButtonIconInfo(expectedLargeIcon);
		EasyMock.expect(iconSource.getIcon(iconKey)).andReturn(expectedIcon);
		EasyMock.expect(iconSource.getIcon(largeIconKey)).andReturn(expectedLargeIcon);
		configurable.setIconInfo(expectedIconInfo);
		configurable.setLargeIconInfo(expectedLargeIconInfo);

		EasyMock.replay(iconSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(iconSource);
		EasyMock.verify(configurable);
	}

	@Test
    public void testCommandIconConfigurableWithNoIconFound() {

		String objectName = "bogusCommandIconConfigurable";
		String iconKey = objectName + ".icon";
		String largeIconKey = objectName + ".large.icon";

		// Create the required mock objects
		IconSource iconSource = (IconSource) EasyMock.createMock(IconSource.class);
		CommandIconConfigurable configurable = (CommandIconConfigurable) EasyMock
				.createMock(CommandIconConfigurable.class);

		// Create the configurer with the mock icon source
		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(null, null, iconSource,
				null);

		EasyMock.expect(iconSource.getIcon(iconKey)).andReturn(null);
		EasyMock.expect(iconSource.getIcon(largeIconKey)).andReturn(null);

		EasyMock.replay(iconSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(iconSource);
		EasyMock.verify(configurable);
	}

	/**
	 * Confirms that a {@link ImageConfigurable} object will have its image set
	 * correctly, if the image source can find it using the key
	 * 'objectName.image'.
	 */
	@Test
    public void testImageConfigurable() {

		String objectName = "bogusImageConfigurable";
		String iconKey = objectName + ".image";
		Image expectedImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);

		// Create the required mock objects
		ImageSource imageSource = (ImageSource) EasyMock.createMock(ImageSource.class);
		ImageConfigurable configurable = (ImageConfigurable) EasyMock.createMock(ImageConfigurable.class);

		// Create the configurer with the mock image source
		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(null, imageSource, null,
				null);

		EasyMock.expect(imageSource.getImage(iconKey)).andReturn(expectedImage);
		configurable.setImage(expectedImage);

		EasyMock.replay(imageSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(imageSource);
		EasyMock.verify(configurable);

	}

	@Test
    public void testImageConfigurableWithNoImageFound() {
		String objectName = "bogusImageConfigurable";
		String iconKey = objectName + ".image";

		// Create the required mock objects
		ImageSource imageSource = (ImageSource) EasyMock.createMock(ImageSource.class);
		ImageConfigurable configurable = (ImageConfigurable) EasyMock.createMock(ImageConfigurable.class);

		// Create the configurer with the mock image source
		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(null, imageSource, null,
				null);

		EasyMock.expect(imageSource.getImage(iconKey)).andReturn(null);

		EasyMock.replay(imageSource);
		EasyMock.replay(configurable);

		configurer.configure(configurable, objectName);

		EasyMock.verify(imageSource);
		EasyMock.verify(configurable);

	}

	/**
	 * Confirms that a {@link SecurityControllable} object will have its
	 * security controller set correctly, as retrieved from a MessageSource
	 * using the key 'beanName.label'.
	 */
	@Test
    public void testSecurityControllable() {

		String objectName = "bogusSecurityControllable";
		String securityControllerId = "bogusSecurityControllerId";

		// create the required mock objects
		SecurityControllerManager controllerManager = (SecurityControllerManager) EasyMock
				.createMock(SecurityControllerManager.class);
		Secured controllable = (Secured) EasyMock.createMock(Secured.class);
		SecurityController controller = (SecurityController) EasyMock.createMock(SecurityController.class);

		// Create the configurer with the mock security controller manager
		DefaultApplicationObjectConfigurer configurer = new DefaultApplicationObjectConfigurer(null, null, null,
				controllerManager);

		// Set the expectations for this run. The security controller manager
		// has not been provided
		// with the controller for the given controller id yet so we don't
		// expect the controllable
		// to be added to the controllerManager as a controlled object
		EasyMock.expect(controllable.getSecurityControllerId()).andReturn(securityControllerId);
		EasyMock.expect(controllerManager.getSecurityController(securityControllerId)).andReturn(null);

		// switch to replay mode...
		EasyMock.replay(controllable);
		EasyMock.replay(controllerManager);
		EasyMock.replay(controller);

		// run the test...
		configurer.configure(controllable, objectName);

		// and verify the results...
		EasyMock.verify(controllable);
		EasyMock.verify(controllerManager);
		EasyMock.verify(controller);

		// reset the mocks for the next part of the test
		EasyMock.reset(controllable);
		EasyMock.reset(controllerManager);
		EasyMock.reset(controller);

		// Set the expectations for the next test. This time we want the
		// controller manager to find
		// the controller for the given id and we expect the controllable to be
		// added to the
		// controller manager as a controlled object
		EasyMock.expect(controllable.getSecurityControllerId()).andReturn(securityControllerId);
		EasyMock.expect(controllerManager.getSecurityController(securityControllerId)).andReturn(controller);
		controller.addControlledObject(controllable);

		// switch to replay mode...
		EasyMock.replay(controllable);
		EasyMock.replay(controllerManager);
		EasyMock.replay(controller);

		// run the test...
		configurer.configure(controllable, objectName);

		// and verify the results
		EasyMock.verify(controllable);
		EasyMock.verify(controllerManager);
		EasyMock.verify(controller);

	}

}

