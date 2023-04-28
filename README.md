# Environment recommendations:
* Project was run on Android Studio Dolphin | 2021.3.1
* Installed **Java 11**

# Task:

### Unit tests
1. Write tests for SearchCountryUseCase class. Make sure that all cases are covered.
   Recommended to use Parametrized test.
2. Write tests for CountriesViewModel. 
   Highly recommend to cover search functionality.

### Ui tests
3. Write tests for CountryDetailsFragment.
4. Write tests for CountryFragment.

**Recommendation**: 
* use Roboelectric to run ui tests on JVM Environment.
* use Espresso to verify views.
* cover androidx navigation calls. 

# Test coverage:

To verify test coverage run next commands:
* for `domain` module: `./gradlew :domain:koverHtmlReport`. Report will be generated in `*/domain/build/reports/html-result/index.html`
* for `presentation` module: `./gradlew :presentation:koverHtmlReportDebug`. Report will be generated in `*/presentation/build/reports/kover/htmlDebug/index.html`

Good result is > **80%** coverage.