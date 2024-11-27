import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ArrayAdapter
import com.example.stopsmoking.R

class TrophiesListAdapter(
    context: Context,
    private var trophies: MutableList<String>,
    private val trophyImagesMap: Map<String, Int>
) : ArrayAdapter<String>(context, 0, trophies) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_trophy, parent, false)

        val trophyTextView = view.findViewById<TextView>(R.id.trophyTextView)
        val trophyImageView = view.findViewById<ImageView>(R.id.trophyImage)

        val trophy = trophies[position]
        trophyTextView.text = trophy
        trophyImageView.setImageResource(trophyImagesMap[trophy] ?: R.drawable.check)

        return view
    }

    fun updateTrophies(newTrophies: List<String>) {
        trophies.clear()
        trophies.addAll(newTrophies)
        notifyDataSetChanged()
    }
}
