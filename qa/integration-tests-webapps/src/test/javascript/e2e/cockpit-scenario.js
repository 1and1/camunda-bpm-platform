describe('googling', function() {

  beforeEach(function() {
    browser().navigateTo('http://google.com');
  });

  it('should be on google page after navigation', function() {
    expect(browser().window().path()).toBe("http://google.com");
  });
});