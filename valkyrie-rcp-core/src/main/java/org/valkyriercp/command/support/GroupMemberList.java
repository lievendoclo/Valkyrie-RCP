package org.valkyriercp.command.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.command.GroupContainerPopulator;
import org.valkyriercp.command.config.CommandButtonConfigurer;

import java.util.*;

public class GroupMemberList {
    private static final Log logger = LogFactory.getLog(GroupMemberList.class);

    private List members = new ArrayList(9);

    private Map builders = new WeakHashMap(6);

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
        Iterator it = builders.values().iterator();
        while (it.hasNext()) {
            GroupMemberContainerManager gcm = (GroupMemberContainerManager)it.next();
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
        Iterator iter = builders.values().iterator();
        while (iter.hasNext()) {
            GroupMemberContainerManager builder = (GroupMemberContainerManager)iter.next();
            if (builder != null) {
                builder.rebuildControlsFor(members);
            }
        }
    }

    public boolean contains(AbstractCommand command) {
        for (int i = 0; i < members.size(); i++) {
            GroupMember member = (GroupMember)members.get(i);
            if (member.managesCommand(command.getId())) {
                return true;
            }
        }
        return false;
    }

}