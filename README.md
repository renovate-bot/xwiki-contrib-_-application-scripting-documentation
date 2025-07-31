# Scripting Documentation

This extension provides scripting documentation by dynamically searching the
current wiki instance for available bindings.

* Project Lead: [Raphaël Jakse](https://www.xwiki.org/xwiki/bin/view/XWiki/rjakse)
* [Documentation & Download](https://extensions.xwiki.org/xwiki/bin/view/Extension/Scripting%20Documentation%20Application)
* [Issue Tracker](https://jira.xwiki.org/browse/SCRIPTDOC)
* Communication: [Forum](https://forum.xwiki.org), [Chat](https://dev.xwiki.org/xwiki/bin/view/Community/Chat)
* [Development Practices](https://dev.xwiki.org/xwiki/bin/view/Community/DevelopmentPractices)
* Minimal XWiki version supported: 13.10
* License: LGPL 2.1
* Translations: N/A
* Sonar Dashboard: N/A
* Continuous Integration Status: N/A

This application provides scripting documentation by dynamically searching the current wiki
instance for available bindings. Using the information from Maven pom's, those bindings get linked to their respective
documentations both on the XWiki Extension Repository and the XWiki Nexus Repository that hold Javadoc packages.
Primitive bindings and non maven compliant ones, like velocity variable and tools could also be documented using simple
descriptions and links to documentation from a localization bundle. An initial bundle is provided with this extension.

Compare to the actual Scripting Reference Documentation, the usage of this application does not require any work
effort to generate the documentation. It is based on what we already release. The current API does not allow access 
to older versions, it is compatible with 4.3 or earlier, but it could be implemented up to version 3.5 where there 
have been a couple of changes that are not easier to cope with. However, it has already a special handling for SNAPSHOT 
version (for which javadoc is not generated) to point to the latest release preceding that SNAPSHOT version.

The UI is based on an AngularJS application and requires 6.2.5 or later, due for the needs of WebJars.
The UI provide access to a part of the information provided by the API. The documentation is displayed integrated into
the XWiki page without using iFrames and support the browser navigation buttons (back/forward). Some links missing in
the Javadoc when crossing projects boundaries are dymically added to improve navigation in the project.
By safety and respect to copyrights of external projects, links to external documentation outside of the XWiki project
are open in a separate window.
