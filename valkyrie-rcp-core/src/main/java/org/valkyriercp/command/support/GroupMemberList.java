package org.valkyriercp.command.support;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.command.GroupContainerPopulator;
import org.valkyriercp.command.config.CommandButtonConfigurer;

import java.util.*;

public class GroupMemberList {
    private static final Log logger = LogFactory.getLog(GroupMemberList.class);

    private List<GroupMember> members = Lists.newArrayList();

    private Map<Object, GroupMemberContainerManager> builders = new WeakHashMap<>(6);

    private ExpansionPointGroupMember expansionPoint;

    public GroupMemberList() {

    }

    public void add(GroupMember member) {
        if (members.add(member)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Member '" + member + "' added to memberList");
            }
            member.onAdded();
        }
    }

    public void append(GroupMember member) {
        getExpansionPoint().add(member);
    }

    public ExpansionPointGroupMember getExpansionPoint() {
        if (expansionPoint == null) {
            expansionPoint = new ExpansionPointGroupMember();
            add(expansionPoint);
        }
        return expansionPoint;
    }

    public int size() {
        return members.size();
    }

    public Iterator iterator() {
        return Collections.unmodifiableList(members).iterator();
    }

    public void setContainersVisible(boolean visible) {
        for (GroupMemberContainerManager gcm : builders.values()) {
            gcm.setVisible(visible);
        }
    }

    protected void bindMembers(Object owner, GroupContainerPopulator container, Object factory,
            CommandButtonConfigurer configurer) {
        GroupMemberContainerManager builder = new GroupMemberContainerManager(container, factory, configurer);
        builder.rebuildControlsFor(members);
        builders.put(owner, builder);
    }

    protected void rebuildControls() {
        for (GroupMemberContainerManager builder : builders.values()) {
            if (builder != null) {
                builder.rebuildControlsFor(members);
            }
        }
    }

    public boolean contains(AbstractCommand command) {
        for (GroupMember member : members) {
            if (member.managesCommand(command.getId())) {
                return true;
            }
        }
        return false;
    }

}