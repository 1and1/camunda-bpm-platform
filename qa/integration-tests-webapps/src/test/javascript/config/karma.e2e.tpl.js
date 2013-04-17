files = [
  ANGULAR_SCENARIO,
  ANGULAR_SCENARIO_ADAPTER,

  '../src/test/javascript/e2e/cockpit-scenario.js',
];

// IE
browsers = ["IE"];

autoWatch = false;
singleRun = true;

junitReporter = {
  outputFile: 'failsafe-reports/e2e.xml',
  suite: 'E2E'
};