package de.neonew.mesh.android

import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.olsrd.*

class OlsrdFragment : Fragment() {

    var handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.olsrd, container, false)

        val olsrd_run = view.findViewById<Button>(R.id.olsrd_run)
        olsrd_run.setOnClickListener {
            Olsrd().run(context)
            update()
        }

        val olsrd_kill = view.findViewById<Button>(R.id.olsrd_kill)
        olsrd_kill.setOnClickListener {
            Olsrd().kill()
            update()
        }

        val olsrd_force_kill = view.findViewById<Button>(R.id.olsrd_force_kill)
        olsrd_force_kill.setOnClickListener {
            Olsrd().forceKill()
            update()
        }

        handler.post(updateRunnable)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateRunnable);
    }

    override fun onResume() {
        super.onResume()

        update()
    }

    fun update() {
        olsrd_running.text = Olsrd.isRunning().toString()
        Olsrd.getNeighbors {
            olsrd_neighbors.text = it.joinToString("\n")
        }
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            update()
            handler.postDelayed(this, 3000)
        }
    }
}
