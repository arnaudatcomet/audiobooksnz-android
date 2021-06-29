package com.audiobookz.nz.app.util

/**
 * Constants used throughout the app.
 */
const val DATA_FILENAME = "sets.json"

enum class SocialID {
    Facebook,
    Google
}

const val FEATURED_PAGE_SIZE = 70
const val CATEGORY_PAGE_SIZE = 50
const val AUDIOBOOKLIST_PAGE_SIZE = 50
const val CLOUDBOOK_PAGE_SIZE = 10
const val FEATURED_BOOK_SHOW = 8
const val REVIEW_PAGE_SIZE = 20
const val DOWNLOAD_COMPLETE = "Download Completed"
const val THIRTY_MILI_SEC = 30000
const val HOUR_MILI_SEC = 3600000
const val MINUTE_MILI_SEC = 60000
const val WEB_URL = "https://audiobooksnz.co.nz"

enum class ConversionEvent {
    add_to_cart,
    add_to_wishlist,
    app_store_subscription_convert,
    app_store_subscription_renew,
    begin_checkout,
    ecommerce_purchase,
    in_app_purchase,
    purchase,
    view_item,
    view_item_list,
    view_search_results,
}
