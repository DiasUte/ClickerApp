package com.example.clickerapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    data class Enemy(val hp: Int, val idleAnim: Int, val deadAnim: Int, val deadDelay: Long)

    private val enemies = listOf(
        Enemy(100,R.drawable.enemy1_idle_animation, R.drawable.enemy1_dead_animation, 350 + 800),
        Enemy(200,R.drawable.enemy2_idle_animation, R.drawable.enemy2_dead_animation, 350 + 800),
        Enemy(300,R.drawable.enemy3_idle_animation, R.drawable.enemy3_dead_animation, 350 + 800),
        Enemy(400,R.drawable.enemy4_idle_animation, R.drawable.enemy4_dead_animation, 350 + 800),
        Enemy(500,R.drawable.enemy5_idle_animation, R.drawable.enemy5_dead_animation, 350 + 800)
    )

    private var currentEnemy = 0
    private val numEnemy = enemies.size

    private var mAnimationDrawable: AnimationDrawable? = null
    private var mAnimationEnemyDrawable: AnimationDrawable? = null

    private var enemyHP = enemies[currentEnemy].hp

    private var mc_damage = 20
    private var bonusDamage = 0

    private var countForBonusDamage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar.max = enemyHP
        progressBar.setProgress(enemyHP)

        //setting idle animation for mc
        mAnimationDrawable?.stop()
        MCView.setBackgroundResource(R.drawable.idle_animation)
        mAnimationDrawable = MCView.getBackground() as AnimationDrawable?
        mAnimationDrawable?.start()

        //setting idle animation for enemy
        mAnimationEnemyDrawable?.stop()
        enemyView.setBackgroundResource(enemies[currentEnemy].idleAnim)
        mAnimationEnemyDrawable = enemyView.getBackground() as AnimationDrawable?
        mAnimationEnemyDrawable?.start()

        attackBtn.setOnClickListener {
            fight()
        }

        bonusDamageBtn.setOnClickListener {
            addBonusDamage()
        }

    }

    fun fight() {
        enemyHP -= mc_damage + bonusDamage
        if (enemyHP <= 0) {
            enemyHP = 0
        }
        if (bonusDamage > 0) {
            countForBonusDamage = 0
            bonusDamage = 0
        }
        progressBar.setProgress(enemyHP)



        if (enemyHP <= 0) {
            //start die animation
            mAnimationEnemyDrawable?.stop()
            enemyView.setBackgroundResource(enemies[currentEnemy].deadAnim)
            mAnimationEnemyDrawable = enemyView.getBackground() as AnimationDrawable?
            mAnimationEnemyDrawable?.start()

            val handler = Handler()
            handler.postDelayed({
                nextEnemy()
            }, enemies[currentEnemy].deadDelay)

        }

        mAnimationDrawable?.stop()
        MCView.setBackgroundResource(R.drawable.attack_animation)
        mAnimationDrawable = MCView.getBackground() as AnimationDrawable?
        mAnimationDrawable?.start()
        val handler = Handler()
        handler.postDelayed({
            enemyView.setImageResource(R.color.transparent)

            //setting idle animation for mc
            mAnimationDrawable?.stop()
            MCView.setBackgroundResource(R.drawable.idle_animation)
            mAnimationDrawable = MCView.getBackground() as AnimationDrawable?
            mAnimationDrawable?.start()
        }, 400)

        countForBonusDamage++
        if (countForBonusDamage == 5) {
            bonusDamageBtn.visibility = View.VISIBLE
        }
    }

    fun addBonusDamage() {
        bonusDamage = listOf(30, 40, 50, 60, 70,100,5,10,25).random()
        bonusDamageBtn.visibility = View.INVISIBLE
    }

    fun nextEnemy() {
        mAnimationEnemyDrawable?.stop()
        currentEnemy++
        if (currentEnemy === numEnemy) {
            val intent : Intent = Intent(this, WinActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            enemyHP = enemies[currentEnemy].hp
            progressBar.max = enemyHP
            progressBar.setProgress(enemyHP)

            //setting idle animation for enemy
            mAnimationEnemyDrawable?.stop()
            enemyView.setBackgroundResource(enemies[currentEnemy].idleAnim)
            mAnimationEnemyDrawable = enemyView.getBackground() as AnimationDrawable?
            mAnimationEnemyDrawable?.start()
        }
    }
}