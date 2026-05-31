$env:BROWSERSTACK_USERNAME = "your_username"
$env:BROWSERSTACK_ACCESS_KEY = "your_key"
./gradlew :test --stacktrace 2>&1 | Select-String -Pattern "IllegalArgument|capabilities|invalid|W3C" -Context 3,3