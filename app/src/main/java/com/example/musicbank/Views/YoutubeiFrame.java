package com.example.musicbank.Views;

public class YoutubeiFrame {
    public static String frame_youtube(String id, int second) {
        String frameVideo2 = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <style type=\"text/css\">\n" +
                "      html {\n" +
                "        height: 100%;\n" +
                "      }\n" +
                "      body {\n" +
                "        min-height: 100%;\n" +
                "        margin: 0;\n" +
                "      }\n" +
                "      iframe {\n" +
                "        position: absolute;\n" +
                "        border: none;\n" +
                "        height: 100%;\n" +
                "        width: 100%;\n" +
                "        top: 0;\n" +
                "        left: 0;\n" +
                "        bottom: 0;\n" +
                "        right: 0;\n" +
                "      }\n" +
                "      #overlay { position: absolute; z-index: 3; opacity: 0.5; filter: alpha(opacity = 50); top: 0; bottom: 0; left: 0; right: 0; width: 100%; height: 100%; background-color: Black; color: White; display: none;}\n" +
                "    </style>\n" +
                "  <body style='margin:0;padding:0'>\n" +
                "    <!-- 1. The <iframe> (and video player) will replace this <div> tag. -->\n" +
                "    <div id=\"player\" ></div>\n" +
                "\n" +
                "    <script>\n" +
                "      // 2. This code loads the IFrame Player API code asynchronously.\n" +
                "      var tag = document.createElement('script');\n" +
                "\n" +
                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "\n" +
                "      // 3. This function creates an <iframe> (and YouTube player)\n" +
                "      //    after the API code downloads.\n" +
                "      var player;\n" +
                "      function onYouTubeIframeAPIReady() {\n" +
                "        player = new YT.Player('player', {\n" +
                "          height: '100%',\n" +
                "          width: '100%',\n" +
                "          playerVars: {'controls': 0, 'showinfo':0, 'start': " + second + " },\n" +
                "          videoId: '" + id + "',\n" +
                "          events: {\n" +
                "            'onReady': onPlayerReady,\n" +
                "            'onStateChange': onPlayerStateChange\n" +
                "          }\n" +
                "        });\n" +
                "      }\n" +
                "\n" +
                "   function getCurrentTime(){\n" +
                "       Android.notifyCurrentTime(player.getCurrentTime());\n" +
                "      }\n" +
                "      function getDuration(){\n" +
                "        Android.notifyDuration(player.getDuration());\n" +
                "      }\n" +
                "      // 4. The API will call this function when the video player is ready.\n" +
                "      function onPlayerReady(event) {\n" +
                "        event.target.playVideo();\n" +
                "      }\n" +
                "\n" +
                "      // 5. The API calls this function when the player's state changes.\n" +
                "      //    The function indicates that when playing a video (state=1),\n" +
                "      //    the player should play for six seconds and then stop.\n" +
                "      var done = false;\n" +
                "      function onPlayerStateChange(event) {\n" +
                "        Android.playerStateChange(event.data);\n" +
                "      }\n" +
                "      function stopVideo() {\n" +
                "        player.stopVideo();\n" +
                "      }\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";

        return frameVideo2;
    }
}