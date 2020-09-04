Vue.component('app-header', {
	props: ["badge", "active"],
	template:
		`<header class="mdl-layout__header mdl-layout__header--waterfall portfolio-header">
		<div class="mdl-layout__header-row portfolio-logo-row">
			<div class="portfolio-logo"></div>
		</div>
		<div class="mdl-layout__header-row portfolio-navigation-row mdl-layout--large-screen-only">
			<nav class="mdl-navigation mdl-typography--body-1-force-preferred-font">
				<a href="index.html" class="mdl-navigation__link" :class="{'is-active':active=='index'}">Shop</a>
				<a href="cart.html"  class="mdl-navigation__link" :class="{'is-active':active=='cart'}">
					<span class="mdl-badge" :data-badge="badge">Warenkorb</span>
				</a>
				<a v-if="badge>0" href="checkout.html" class="mdl-navigation__link" :class="{'is-active':active=='checkout'}">
					Zur Kasse
					<i class="material-icons">exit_to_app</i>
				</a>
				<a href="about.html" class="mdl-navigation__link" :class="{'is-active':active=='about'}">Über uns</a>
				<span>
					Sprache<br/>
					<select id="locales" onchange="location.search='?lang='+this.value">
						<option value="">-- Wählen --</option>
						<option value="en">Englisch</option>
						<option value="de" selected="selected">Deutsch</option>
					</select>
				</span>
				<a href="/login" class="mdl-navigation__link">Anmelden</a>
			</nav>
		</div>
	</header>`
})
Vue.component('drawer', {
	template: `<div class="mdl-layout__drawer mdl-layout--small-screen-only">
	<nav class="mdl-navigation mdl-typography--body-1-force-preferred-font">
		<a class="mdl-navigation__link" href="index.html">Shop</a>
		<a class="mdl-navigation__link" href="cart.html">Warenkorb</a>
		<a class="mdl-navigation__link" href="checkout.html">Zur Kasse</a>
		<a class="mdl-navigation__link" href="about.html">Über uns</a>
		<a class="mdl-navigation__link" href="/login">Anmelden</a>
	</nav>
</div>`
})
Vue.component('app-footer', {
template:
`<footer class="mdl-mini-footer">
	<div class="mdl-mini-footer__left-section">
		<div class="mdl-logo">Produkt-Shop</div>
	</div>
	<div class="mdl-mini-footer__right-section">
		<ul class="mdl-mini-footer__link-list">
			<li><a href="#">Hilfe</a></li>
			<li><a href="#">Impressum</a></li>
		</ul>
	</div>
</footer>`
})