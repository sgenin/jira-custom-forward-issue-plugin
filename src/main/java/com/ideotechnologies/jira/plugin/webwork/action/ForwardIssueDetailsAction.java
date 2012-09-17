package com.ideotechnologies.jira.plugin.webwork.action;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.exception.IssueNotFoundException;
import com.atlassian.jira.exception.IssuePermissionException;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueFactory;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.IssueLinkTypeManager;
import com.atlassian.jira.issue.link.RemoteIssueLinkManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.web.action.issue.CloneIssueDetails;
import com.atlassian.jira.web.action.issue.IssueCreationHelperBean;

public class ForwardIssueDetailsAction extends CloneIssueDetails {

	public ForwardIssueDetailsAction(ApplicationProperties applicationProperties,
			PermissionManager permissionManager,
			IssueLinkManager issueLinkManager,RemoteIssueLinkManager remoteIssueLinkManager,
			IssueLinkTypeManager issueLinkTypeManager,
			SubTaskManager subTaskManager, AttachmentManager attachmentManager,
			FieldManager fieldManager,
			IssueCreationHelperBean issueCreationHelperBean,
			IssueFactory issueFactory, IssueService issueService) {
		super(applicationProperties, permissionManager, issueLinkManager,
				remoteIssueLinkManager, issueLinkTypeManager, subTaskManager, attachmentManager, fieldManager,
				issueCreationHelperBean, issueFactory, issueService);

	}
	@Override
	// Initialise the clone issue with the data from the original issue
    public String doDefault() throws Exception
    {
        super.setCloneSubTasks(false);
        super.setCloneLinks(false);
        super.setCloneAttachments(false);

        try
        {
            Issue issueObject = getIssueObject(getIssue());
            setOriginalIssue(issueObject);

            // Copy the details of the original issue for the clone issue
            setIssueDetails(issueObject);
        }
        catch (IssueNotFoundException e)
        {
            // Error is added above
            return ERROR;
        }
        catch (IssuePermissionException e)
        {
            return ERROR;
        }

        // Summary can be modified - require futher input
        return INPUT;
    }
	@Override
	  public String getCloneLinkTypeName()
	    {
	        return "Forward";
	    }
@Override
protected String doPostCreationTasks() throws Exception {
	
	if (getCloneParent() != null)
         return returnCompleteWithInlineRedirect("/secure/MoveIssue!default.jspa?id="+getCloneParent().getString("id"));
	 else
		 return returnCompleteWithInlineRedirect("/secure/MoveIssue!default.jspa?id="+getIssueObject().getId().toString());
}
}
