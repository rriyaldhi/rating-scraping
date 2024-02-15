const puppeteer = require('puppeteer-core');
const AUTH = 'brd-customer-hl_8f6cf56b-zone-scraping_browser:yzo0qmw801xa';
const SBR_WS_ENDPOINT = `wss://${AUTH}@brd.superproxy.io:9222`;

async function main() {
    console.log('Connecting to Scraping Browser...');
    const browser = await puppeteer.connect({
        browserWSEndpoint: SBR_WS_ENDPOINT,
    });
    try {
        console.log('Connected! Navigating...');
        const page = await browser.newPage();
        await page.goto('https://www.tripadvisor.com/AttractionProductReview-g255060-d11458687-Day_Trip_with_Chef_Led_Hunter_Valley_Gourmet_Food_and_Wine_from_Sydney-Sydney_New_.html', { timeout: 2 * 60 * 1000 });
        console.log('Taking screenshot to page.png');
        await page.screenshot({ path: './page.png', fullPage: true });
        console.log('Navigated! Scraping page content...');
        const html = await page.content();
        console.log(html);
        // CAPTCHA solving: If you know you are likely to encounter a CAPTCHA on your target page, add the following few lines of code to get the status of Scraping Browser's automatic CAPTCHA solver
        // Note 1: If no captcha was found it will return not_detected status after detectTimeout
        // Note 2: Once a CAPTCHA is solved, if there is a form to submit, it will be submitted by default
        // const client = await page.target().createCDPSession();
        // const {status} = await client.send('Captcha.solve', {detectTimeout: 30*1000});
        // console.log(`Captcha solve status: ${status}`)
    } finally {
        await browser.close();
    }
}

if (require.main === module) {
    main().catch(err => {
        console.error(err.stack || err);
        process.exit(1);
    });
}