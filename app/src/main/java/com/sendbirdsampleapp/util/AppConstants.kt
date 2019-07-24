package com.sendbirdsampleapp.util

object AppConstants {

    internal val APP_ID = "B6DCC89A-9D92-4012-B354-CC41A1BAC5DF"

    internal val CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHANNEL_LIST"
    internal val CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_CHAT"

    internal val EMPTY_USER_ID = 1001
    internal val EMPTY_NICKNAME = 1002
    internal val FAILED_LOGIN = 1003
    internal val FAILED_CHANNEL_GET = 1005
    internal val FAILED_CHANNEL_CREATE = 1006
    internal val FAILED_UPDATE_USER = 1007
    internal val FAILED_FIREBASE_CONNECTION = 1008

    //Constant viewTypes for chat
    internal val VIEW_TYPE_USER_MESSAGE_ME = 1
    internal val VIEW_TYPE_USER_MESSAGE_OTHER = 2
    internal val VIEW_TYPE_ADMIN_MESSAGE = 3
    internal val VIEW_TYPE_VIDEO_MESSAGE_ME = 5
    internal val VIEW_TYPE_VIDEO_MESSAGE_OTHER = 6
    internal val VIEW_TYPE_IMAGE_MESSAGE_ME = 7
    internal val VIEW_TYPE_IMAGE_MESSAGE_OTHER = 8
    internal val VIEW_TYPE_FILE_MESSAGE_ME = 9
    internal val VIEW_TYPE_FILE_MESSAGE_OTHER = 10
}