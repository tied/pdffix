package dk.schneiderelectric.dcimteam.pdffix.rest;

import com.atlassian.confluence.importexport.ImportExportManager;
import com.atlassian.confluence.pages.ContentTree;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * A resource of message.
 */
@Path("/")
@Scanned
public class PDFFixResource {

    @ComponentImport
    private final SpaceManager spaceManager;
    @ComponentImport
    private final ImportExportManager importExportManager;

    @Inject
    public PDFFixResource(SpaceManager spaceManager, ImportExportManager importExportManager) {
        this.spaceManager = spaceManager;
        this.importExportManager = importExportManager;
    }

    @Path("/contentTree")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getPageTtree(@QueryParam("key") String spaceKey) {
        Space space=spaceManager.getSpace(spaceKey);
        ConfluenceUser user= AuthenticatedUserThreadLocal.get();
        ContentTree contentTree=importExportManager.getContentTree(user, space);
        Map<String,Object> context=getDefaultVelocityContext();
        context.put("contentTree", contentTree);
        return Response.ok().entity(renderTemplate("/templates/export-space-common-tree.vm", context)).build();
    }

    private Map<String, Object> getDefaultVelocityContext() {
        return MacroUtils.defaultVelocityContext();
    }

    private String renderTemplate(String templateName,
                                 Map<String, Object> context) {
        return VelocityUtils.getRenderedTemplate(templateName, context);
    }
}