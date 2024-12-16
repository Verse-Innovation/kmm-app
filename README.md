App project provides insights about 
    1. Application Bundle
    2. TBF

**Usage**
 - The library comes up with an <AppBundleLibraryInitializer> which initializes <AppBundleLibrary>. 
 - By using <AppBundleLibrary>, the client application can get the information about <AppBundle>. 
 - Like any other **TagD** library, The <AppBundleLibrary> can be initiazed with a given <Scope> and an alias name for the same to access in the Scope. 
 - The application can update the <AppBundle>'s progressive state and persist the same for the lateral access.
 - The library provides a notifying mechanism to let the <AppBundleUpdateListener> aware that, the <AppBundle> got updated. 

**Features**
 - The <AppBundle> provides the information about
    1. namespace
    2. versionName
    3. currentVersionCode
    4. previousVersionCode
    5. flavour
    6. flavorDimension
    7. buildType
    8. profilable
    9. installTime
    10. installIdentifier
    11. publishingIdentifier
    12. themeLabel
    13. appLocale
    14. systemLocale
 - Also, not just limiting the above information, the <AppBundle> offers Key-Value mechanism to feed in and accesssolution specific app bundle information. 
 
