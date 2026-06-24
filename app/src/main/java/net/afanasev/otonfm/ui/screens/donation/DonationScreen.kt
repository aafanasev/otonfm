package net.afanasev.otonfm.ui.screens.donation

import androidx.compose.runtime.Composable
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.models.StoreTransaction
import com.revenuecat.purchases.ui.revenuecatui.PaywallDialog
import com.revenuecat.purchases.ui.revenuecatui.PaywallDialogOptions
import com.revenuecat.purchases.ui.revenuecatui.PaywallListener
import net.afanasev.otonfm.util.log.Logger

@Composable
fun DonationPaywallDialog(
    onDismiss: () -> Unit,
) {
    PaywallDialog(
        paywallDialogOptions = PaywallDialogOptions.Builder()
            .setDismissRequest(onDismiss)
            .setListener(object : PaywallListener {
                override fun onPurchaseCompleted(
                    customerInfo: CustomerInfo,
                    storeTransaction: StoreTransaction,
                ) {
                    Logger.onDonationPurchase(storeTransaction.productIds.firstOrNull() ?: "")
                    onDismiss()
                }

                override fun onPurchaseError(error: PurchasesError) {
                    Logger.onDonationError(error.message)
                }
            })
            .build(),
    )
}
