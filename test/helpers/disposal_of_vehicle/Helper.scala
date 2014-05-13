package helpers.disposal_of_vehicle

import scala.annotation.tailrec
import play.api.mvc.{Cookie, Cookies, SimpleResult}
import play.api.test.Helpers._

object Helper {
  def countSubstring(str1:String, str2:String):Int={
    @tailrec def count(pos:Int, c:Int):Int={
      val idx=str1 indexOf(str2, pos)
      if(idx == -1) c else count(idx+str2.size, c+1)
    }
    count(0,0)
  }

  /*def getCookies(r: SimpleResult): Seq[Cookie] = {
    r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
  }*/
}