# Domain Module Consumer ProGuard Rules
#
# This module contains only plain models, repository interfaces, and use cases.
# Everything is referenced directly by the app and data modules, so R8 can
# freely shrink, inline, and obfuscate these classes.
