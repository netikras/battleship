import { SbattleUiPage } from './app.po';

describe('sbattle-ui App', () => {
  let page: SbattleUiPage;

  beforeEach(() => {
    page = new SbattleUiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
