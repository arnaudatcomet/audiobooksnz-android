package com.audiobookz.nz.app.more.ui

import android.app.Dialog
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.ListItemCardBinding
import com.audiobookz.nz.app.more.data.CardData
import com.audiobookz.nz.app.more.data.CardDetailData
import com.audiobookz.nz.app.more.data.CardListData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token

class CardListAdapter(
    private var viewModel: MoreViewModel,
    CardList: Map<String, Any?>,
    private var stripe: Stripe,
    private var activity: FragmentActivity?
) :
    RecyclerView.Adapter<CardListAdapter.ViewHolder>() {
    var listCloud = CardList?.get("cloud") as CardListData
    var listLocal = CardList?.get("local") as List<CardData>

    override fun getItemCount(): Int {
        return listCloud.card?.size ?: 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ListItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cloudCard = listCloud.card?.get(position)
        val defaultCard = listCloud.default

        holder.apply {
            bind(
                cloudCard!!,
                hasCardDetail(cloudCard),
                cloudCard.id == defaultCard,
                removeList(cloudCard!!.id),
                setDefaultCard(cloudCard!!.id),
                addCard(cloudCard!!.id)
            )
        }
        holder.itemView.tag = cloudCard
    }

    private fun removeList(cardId: String): View.OnClickListener {
        return View.OnClickListener {
            viewModel.removeCardList(cardId)
        }
    }

    private fun setDefaultCard(cardId: String): View.OnClickListener {
        return View.OnClickListener {
            MaterialAlertDialogBuilder(it.context)
                .setTitle("Set Default Card")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.setDefaultCard(cardId)
                }
                .setNegativeButton("No") { _, _ ->
                }
                .show()
        }
    }

    private fun hasCardDetail(cardData: CardDetailData): Boolean {
        if (listLocal != null) {
            for (local in listLocal) {
                if (cardData.id == local.card_id) {
                    return true
                }
            }
        }

        return false
    }

    private fun addCard(cardId: String): View.OnClickListener {
        return View.OnClickListener {

            var dialog = activity?.let { it1 -> Dialog(it1) }
            dialog?.setContentView(R.layout.card_form_layout)
            var lp: WindowManager.LayoutParams = WindowManager.LayoutParams().apply {
                copyFrom(dialog?.window?.attributes)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }

            val submit = dialog?.findViewById<View>(R.id.submit) as TextView
            val cardNo = dialog.findViewById<View>(R.id.cardNo) as EditText
            val month = dialog.findViewById<View>(R.id.month) as EditText
            val year = dialog.findViewById<View>(R.id.year) as EditText
            val cvv = dialog.findViewById<View>(R.id.cvv) as EditText

            submit.setOnClickListener {
                when {
                    cardNo.length() == 0 || month.length() == 0 || year.length() == 0 || cvv.length() == 0 ->
                        Toast.makeText(
                            activity, "Please fill all the fields", Toast.LENGTH_SHORT
                        ).show()
                    cardNo.length() < 16 -> Toast.makeText(
                        activity, "Please enter" +
                                " valid Card No.", Toast.LENGTH_SHORT
                    ).show()
                    else -> {
                        validateCard(
                            cardId,
                            cardNo.text.toString(),
                            month.text.toString(),
                            year.text.toString(),
                            cvv.text.toString()
                        )
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
            dialog.window?.attributes = lp

        }
    }

    private fun validateCard(
        cardId: String,
        number: String,
        month: String,
        year: String,
        cvc: String
    ) {
        val card =
            Card.create(
                number = number,
                cvc = cvc,
                expMonth = Integer.valueOf(month),
                expYear = Integer.valueOf(year)
            )

        stripe.createCardToken(card, callback = object : ApiResultCallback<Token> {
            override fun onError(e: Exception) {
                Toast.makeText(
                    activity, "${e.message}", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onSuccess(result: Token) {
                viewModel.addCardLocal(cardId, number, cvc, month, year)
            }
        })
    }

    class ViewHolder(private val binding: ListItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CardDetailData,
            hasDetail: Boolean,
            default: Boolean,
            removeClick: View.OnClickListener,
            setDefaultClick: View.OnClickListener,
            enteringDetail: View.OnClickListener
        ) {
            binding.apply {
                card = item
                hasCardInfo = hasDetail
                isDefault = default
                if (default){
                    cardBandTxt.setTypeface(null, Typeface.BOLD)
                }
                remove = removeClick
                setCardDefault = setDefaultClick
                inputCardInfo = enteringDetail
                executePendingBindings()
            }
        }
    }


}