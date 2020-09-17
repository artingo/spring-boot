const WARENKORB='warenkorb', LANG='lang', MESSAGES='messages_'
const index = location.search.indexOf('lang=')
const	lang = (index==-1)? localStorage.getItem(LANG)||'de' : location.search.substring(index + 5, index + 7).toLowerCase()
const i18n = new VueI18n({
	locale: lang,
	messages: {},
})

function loadLanguage(locale) {
	locale = locale || lang
	const msgStorage = sessionStorage.getItem(MESSAGES + locale)
	if (msgStorage) {
		console.log('loading from sessionStorage')
		setMessages(locale, JSON.parse(msgStorage))
		return new Promise(resolve => setTimeout(resolve, 0))
	} else {
		return fetch("/app/messages?lang=" + locale, {headers: {"Content-Type": "application/json"}})
			.then(res => res.json())
			.then(msgServer => {
				console.log('loading from Server')
				setMessages(locale, msgServer)
				sessionStorage.setItem(MESSAGES + locale, JSON.stringify(msgServer))
			})
	}
}
function setMessages(locale, messages) {
	i18n.setLocaleMessage(locale, messages)
	i18n.locale = locale
	localStorage.setItem(LANG, locale)
	document.querySelector('html').setAttribute('lang', locale)
}

const AppHeader = {
	name: 'app-header',
	i18n,
	template:
	`<header class="mdl-layout__header mdl-layout__header--waterfall portfolio-header">
		<div class="mdl-layout__header-row portfolio-logo-row">
				<h1 v-t="'title.shop'"></h1>
		</div>
		<div class="mdl-layout__header-row portfolio-navigation-row mdl-layout--large-screen-only">
			<nav class="mdl-navigation mdl-typography--body-1-force-preferred-font">
				<a href="index.html" class="mdl-navigation__link" :class="{'is-active':active=='index'}" v-t="'nav.portfolio'"></a>
				<a href="cart.html"  class="mdl-navigation__link" :class="{'is-active':active=='cart'}">
					<span class="mdl-badge" :data-badge="cartItems" v-t="'nav.cart'"></span>
				</a>
				<a v-if="cartItems>0"	href="checkout.html" class="mdl-navigation__link" 
					:class="{'is-active':active=='checkout'}">
					{{$t('button.checkout')}}
					<i class="material-icons">exit_to_app</i>
				</a>
				<a href="orders.html" class="mdl-navigation__link" :class="{'is-active':active=='orders'}" v-t="'nav.orders'"></a>
				<a href="about.html" class="mdl-navigation__link" :class="{'is-active':active=='about'}" v-t="'nav.about'"></a>
				<span>
					{{$t('lang.change')}}<br/>
					<select id="locales" v-model="lang">
						<option value="en" v-t="'lang.en'"></option>
						<option value="de" v-t="'lang.de'"></option>
					</select>
				</span>
				<a href="/login" class="mdl-navigation__link" v-t="'nav.login'"></a>
			</nav>
		</div>
	</header>`,
	props: ['active', 'title', 'badge'],
	data: function() {
		return {
			lang: lang,
			warenkorb: {produkte: []}
		}
	},
	watch: {
		lang: function(newValue) {
			loadLanguage(newValue).then(() => document.title = i18n.t(this.title))
		}
	},
	computed: {
		cartItems: function() {
			return this.badge || this.warenkorb.produkte.length
		}
	},
	created: function() {
		let localWarenkorb = sessionStorage.getItem(WARENKORB)
		if (localWarenkorb) {
			this.warenkorb = JSON.parse(localWarenkorb)
		}
		document.title = i18n.t(this.title)
	},
	style: ``
}

const Drawer = {
	name: 'drawer',
	i18n,
	template: `<div class="mdl-layout__drawer mdl-layout--small-screen-only">
	<nav class="mdl-navigation mdl-typography--body-1-force-preferred-font">
		<a class="mdl-navigation__link" href="index.html" v-t="'nav.portfolio'"></a>
		<a class="mdl-navigation__link" href="cart.html" v-t="'nav.cart'"></a>
		<a class="mdl-navigation__link" href="checkout.html" v-t="'button.checkout'"></a>
		<a class="mdl-navigation__link" href="orders.html" v-t="'nav.orders'"></a>
		<a class="mdl-navigation__link" href="about.html" v-t="'nav.about'"></a>
		<a class="mdl-navigation__link" href="/login" v-t="'nav.login'"></a>
	</nav>
</div>`
}

const AppFooter = {
	name: 'app-footer',
	i18n,
	template:
	`<footer class="mdl-mini-footer">
		<div class="mdl-mini-footer__left-section">
			<div class="mdl-logo" v-t="'footer.title'"></div>
		</div>
		<div class="mdl-mini-footer__right-section">
			<ul class="mdl-mini-footer__link-list">
				<li><a href="#" v-t="'footer.help'"></a></li>
				<li><a href="#" v-t="'footer.imprint'"></a></li>
			</ul>
		</div>
	</footer>`
}