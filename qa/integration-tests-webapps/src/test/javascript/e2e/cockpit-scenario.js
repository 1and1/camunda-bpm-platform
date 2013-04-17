describe('googling', function() {

  beforeEach(function() {
    browser().navigateTo('/google');
  });

  it('should be on google page after navigation', function() {
    expect(browser().window().path()).toBe("/google");
  });
});