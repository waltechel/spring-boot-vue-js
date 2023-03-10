module.exports = {
  // For authoring Nightwatch tests, see https://nightwatchjs.org/guide
  'login test' : browser => {
    browser
      .url(process.env.VUE_DEV_SERVER_URL + 'login')
      .waitForElementVisible('#app', 5000)
      .assert.containsText('h1', 'TaskAgile')
      .end()
  }
}
