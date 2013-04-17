files = [
  ANGULAR_SCENARIO,
  ANGULAR_SCENARIO_ADAPTER,

  '../e2e/cockpit-scenario.js',
];

// IE
browsers = ["Firefox"];

autoWatch = false;
singleRun = true;

junitReporter = {
  outputFile: '../../../../target/failsafe-reports/e2e.xml',
  suite: 'E2E'
};