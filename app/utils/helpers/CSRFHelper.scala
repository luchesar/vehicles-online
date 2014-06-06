package utils.helpers

import play.api.templates.{Html, HtmlFormat}

object CSRFHelper {

  def hiddenFormField(implicit token: services.csrf_prevention.CSRF.Token): Html = {
    Html(s"""<input type="hidden" name="${services.csrf_prevention.CSRFAction.tokenName}" value="${HtmlFormat.escape(token.value)}"/>""")
  }

}