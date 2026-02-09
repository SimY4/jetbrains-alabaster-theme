<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Alabaster Theme Changelog

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security

## 0.3.9 - 2026-02-09

This release contains fixes to declaration highlighting for non-bg themes.

## 0.3.8 - 2026-02-09

This release ports some of the ideas from the most recent blogpost about Clojure highlighting into IntelliJ. These changes only affect Clojure language support:

- Clojure parenthesis and brackets are now highlighted grey (all except topmost pair of braces which are still highlighted black)
- Clojure namespace prefixes are now also greyed out
- Clojure characters are now highlighted as constants
- Adjust colours of string, comment, declaration and metadata elements
- Simplified theme template by removing unnecessary option overrides
- Custom language comments and number literal highlighting fixes
- Kotlin punctuation highlighting fixes

## 0.3.7 - 2025-12-31

Minor changes in this release:

- De-bald brackets around groovy closures.
- Scala enum singleton values highlighted as constants.
- Inlay hints in a non-BG theme now has a slight background accent to help with highlighting Kotlin's generated code.

## 0.3.6 - 2025-12-09

Minor changes in this release:

- Tone down JavaScript member functions highlighting.
- Scala named parameters highlighting fixes.
- Updated to latest intellij-platform-plugin-template
- Updated to pebble 4.0.0

## 0.3.5 - 2025-11-23

This release adds Rust and Clojure annotators to help with highlighting. Both Rust and Clojure received an uplift with many adjustments to actualize their highlighting.

Other changes in this release:

- Reverted changes to selection background to fix the regression in code completion selection visibility.
- Project search colors now match in-file search colors
- Static final fields are not highlighted as constants.

## 0.3.4 - 2025-11-22

This release is focusing on improving search and editor interfaces highlighting.

And a few other changes:

- Added unmatched braces highlighting
- Highlight enum constants

## 0.3.3 - 2025-11-19

This release is mainly focuses on improving string and regex highlighting in regards to valid and invalid escape sequences. Highlighting behavior should now follow the original theme more closely.

And also brings a few fixes:

- Change highlighting of unused to be similar to deprecated highlighting not to mix it with metadata highlighting.
- JavaScript fields are no longer highlighted.
- Fixed XML attribute highlighting in BG

## 0.3.2 - 2025-11-16

This release adds highlighting for deprecated and unused chunks of code.

Fixes the background ordering issue causing text to appear with different unmatched backgrounds.

And fixes and issue with Go annotator was not being property registered.

## 0.3.1 - 2025-11-12

A quick follow up addressing missing language attribute on annotator configurations.

And two new additional annotators for Python and Go.

## 0.3.0 - 2025-11-12

Third attempt to add annotator to `true`, `false` and `null` keywords. Java, JavaScript, Kotlin and Scala made it into first cut. As always, feedback and bug reports are welcome.

Additionally, fixed Kotlin name arguments highlighting.

## 0.2.8 - 2025-11-10

### Fixed

- Fixed debugger colours once more.

## 0.2.7 - 2025-11-10

### Changed

- VCS current branch commit highlighting is not blueish anymore.

## 0.2.6 - 2025-11-08

### Fixed

- JS and Python decorators highlighting
- Python predefined definitions highlighting
- Fixed regex highlighting
- Fixed interpolated strings highlighting in several languages
- Fixed closure braces boldness
- Fixed Kotlin properties highlighting
- Fixed Kotlin colon highlighting
- Fixed Go highlighting for calls vs declarations

## 0.2.5 - 2025-11-08

### Fixed

- Editor background colors not having an effect

## 0.2.4 - 2025-11-08

### Changed

- Errors highlighting to be more subtle
- More fixes to calls vs declaration highlighting

## 0.2.3 - 2025-11-05

### Added

- New template for generating theme JSON files for all different variants and flavors of the theme.

### Fixed

- Fixed errors highlighting
- Fixed ctrl clickable highlighting
- Fixed sidebar being hardly distinguishable from editor background

## 0.2.2 - 2025-11-04

### Changed

- Islands support for Alabaster Themes
- Metadata highlighting

### Fixed

- Fixed regex and block comment tag highlighting
- Fixed debugger colours
- Fixed highlighting of matching braces
- Fixed highlighting for search results
- Fixes for XPath and Shell script highlighting
- Dim down file colours

## 0.2.1 - 2025-11-03

### Changed

- New templating engine for generating color schemes across different variants and flavors of the theme.
- More work on porting Alabaster Themes

## 0.2.0 - 2025-10-29

### Added

- Alabaster Theme plugin icon

### Changed

- More work on porting Alabaster Themes

### Fixed

- Release pipeline
- Theme and colour scheme name clashes

## 0.1.0 - 2025-10-27

### Added

- Initial port of Alabaster Themes by Tonsky from [Alabaster Color Scheme](https://github.com/tonsky/intellij-alabaster)
