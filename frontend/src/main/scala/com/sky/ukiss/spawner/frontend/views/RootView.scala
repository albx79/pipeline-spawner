package com.sky.ukiss.spawner.frontend.views

import com.sky.ukiss.spawner.frontend.routing.RootState
import com.sky.ukiss.spawner.frontend.services.TranslationsService
import com.sky.ukiss.spawner.shared.css.GlobalStyles
import com.sky.ukiss.spawner.shared.i18n.Translations
import io.udash._
import io.udash.bootstrap.UdashBootstrap.ComponentId
import io.udash.bootstrap.button.{ButtonStyle, UdashButton}
import io.udash.bootstrap.{BootstrapStyles, UdashBootstrap}
import io.udash.css.CssView
import io.udash.i18n.Lang

class RootViewFactory(translationsService: TranslationsService) extends StaticViewFactory[RootState.type](
  () => new RootView(translationsService)
)

class RootView(translationsService: TranslationsService) extends ContainerView with CssView {
  import scalatags.JsDom.all._

  private def langChangeButton(lang: Lang): Modifier  = {
    val btn = UdashButton(
      buttonStyle = ButtonStyle.Link, componentId = ComponentId(s"lang-btn-${lang.lang}")
    )(lang.lang.toUpperCase())

    btn.listen {
      case UdashButton.ButtonClickEvent(_, _) =>
        translationsService.setLanguage(lang)
    }

    btn.render
  }

  // ContainerView contains default implementation of child view rendering
  // It puts child view into `childViewContainer`
  override def getTemplate: Modifier = div(
    // loads Bootstrap and FontAwesome styles from CDN
    UdashBootstrap.loadBootstrapStyles(),
    UdashBootstrap.loadFontAwesome(),

    BootstrapStyles.container,
    div(
      GlobalStyles.floatRight,
      Translations.langs.map(v => langChangeButton(Lang(v)))
    ),
    h1("Pipeline Spawner"),
    childViewContainer
  )
}