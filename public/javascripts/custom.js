require.config({
    paths: {
        'jquery': 'jquery/jquery-1.9.1',
        'jquery-migrate': 'jquery/jquery-migrate-1.2.1.min',
        'header-footer-only': 'gov/header-footer-only'
        //'stageprompt': 'stageprompt-2.0.1'
    }
});

require(["jquery", "jquery-migrate", "header-footer-only"],function($) {

    var IE10 = (navigator.userAgent.match(/(MSIE 10.0)/g) ? true : false);
    if (IE10) {
        $('html').addClass('ie10');
    }

    $(function() {
        // view more / view less
        /*
        $('.helper-more').click(function(){
            $(this).toggleClass("helper-less")
            $(this).next(".helper-info").slideToggle("medium");

            if ($(this).text() === 'Close')
            {
                $(this).text('Show example');
            }
            else
            {
                $(this).text('Close');
            }
        });
        */

        // Nino auto jump
        // $('.ni-number input, .sort-code input').autotab_magic();

        // Disabled clicking on disabled buttons
        $('.button-not-implemented').click(function() {
            return false;
        });

        // Print button
        $('.print-button').click(function() {
            window.print();
            return false;
        });

        // smooth scroll
        $('a[href^="#"]').bind('click.smoothscroll', function (e) {
            e.preventDefault();
            var target = this.hash,
                $target = $(target);
            $('html, body').animate({
                scrollTop: $(target).offset().top - 40
            }, 750, 'swing', function () {
                window.location.hash = target;
            });
        });

    });

    /*
    $(document).ready(function() {
        if (typeof console !== 'undefined') {
            console.log("document on ready " + areCookiesEnabled() );
        }

        if (areCookiesEnabled())  {
            $('#no-cookies').toggle(false);
            $('#content').toggle(true);
        }else{
            $('#no-cookies').toggle(true);
            $('#content').toggle(false);
        }
    });
    */



    function areCookiesEnabled(){
        var cookieEnabled = (navigator.cookieEnabled) ? true : false;

        if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled)
        {
            document.cookie="testcookie";
            cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
        }
        return (cookieEnabled);
    }

    function trackEvent(category, action, label, value, noninteraction){
        _gaq.push(['_trackEvent',category,action].concat(opt(label)).concat(opt(value)).concat(opt(noninteraction)));
    }

    function opt(v){
        if (typeof v == 'undefined') return [];
        else return[v];
    }
});
