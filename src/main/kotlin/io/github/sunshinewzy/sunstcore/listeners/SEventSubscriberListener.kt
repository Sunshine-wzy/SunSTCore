package io.github.sunshinewzy.sunstcore.listeners

import io.github.sunshinewzy.sunstcore.events.SMachineAddEvent
import io.github.sunshinewzy.sunstcore.events.SMachineRemoveEvent
import io.github.sunshinewzy.sunstcore.events.SMenuClickEvent
import io.github.sunshinewzy.sunstcore.events.SMenuOpenEvent
import io.github.sunshinewzy.sunstcore.utils.SEventSubscriber
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.entity.*
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.player.*
import org.bukkit.event.server.*
import org.bukkit.event.vehicle.*
import org.bukkit.event.weather.LightningStrikeEvent
import org.bukkit.event.weather.ThunderChangeEvent
import org.bukkit.event.weather.WeatherChangeEvent
import org.bukkit.event.world.*
import org.spigotmc.event.entity.EntityDismountEvent
import org.spigotmc.event.entity.EntityMountEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent

abstract class SEventSubscriberListener : Listener {
    fun <E: Event> call(e: E) {
        SEventSubscriber.callSubscribeEvent(e)
    }
}

object SEventSubscriberListener1 : SEventSubscriberListener() {
    
    //region PlayerEvent
    
    @EventHandler
    fun onPlayerAdvancementDone(e: PlayerAdvancementDoneEvent) { call(e) }

    @EventHandler
    fun onPlayerAnimation(e: PlayerAnimationEvent) { call(e) }

    @EventHandler
    fun onPlayerBedEnter(e: PlayerBedEnterEvent) { call(e) }

    @EventHandler
    fun onPlayerBedLeave(e: PlayerBedLeaveEvent) { call(e) }

    @EventHandler
    fun onPlayerBucketEmpty(e: PlayerBucketEmptyEvent) { call(e) }

    @EventHandler
    fun onPlayerBucketFill(e: PlayerBucketFillEvent) { call(e) }

    @EventHandler
    fun onPlayerChangedMainHand(e: PlayerChangedMainHandEvent) { call(e) }

    @EventHandler
    fun onPlayerChangedWorld(e: PlayerChangedWorldEvent) { call(e) }

    @EventHandler
    fun onPlayerChannel(e: PlayerChannelEvent) { call(e) }

    @EventHandler
    fun onPlayerCommandPreprocess(e: PlayerCommandPreprocessEvent) { call(e) }

    @EventHandler
    fun onPlayerDropItem(e: PlayerDropItemEvent) { call(e) }
    
    @EventHandler
    fun onPlayerEditBook(e: PlayerEditBookEvent) { call(e) }

    @EventHandler
    fun onPlayerEggThrow(e: PlayerEggThrowEvent) { call(e) }
    
    @EventHandler
    fun onPlayerExpChange(e: PlayerExpChangeEvent) { call(e) }
    
    @EventHandler
    fun onPlayerFish(e: PlayerFishEvent) { call(e) }

    @EventHandler
    fun onPlayerGameModeChange(e: PlayerGameModeChangeEvent) { call(e) }

    @EventHandler
    fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) { call(e) }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(e: PlayerInteractEvent) { call(e) }

    @EventHandler
    fun onPlayerItemBreak(e: PlayerItemBreakEvent) { call(e) }

    @EventHandler
    fun onPlayerItemConsume(e: PlayerItemConsumeEvent) { call(e) }

    @EventHandler
    fun onPlayerItemDamage(e: PlayerItemDamageEvent) { call(e) }

    @EventHandler
    fun onPlayerItemHeld(e: PlayerItemHeldEvent) { call(e) }

    @EventHandler
    fun onPlayerItemMend(e: PlayerItemMendEvent) { call(e) }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) { call(e) }

    @EventHandler
    fun onPlayerKick(e: PlayerKickEvent) { call(e) }

    @EventHandler
    fun onPlayerLevelChange(e: PlayerLevelChangeEvent) { call(e) }

    @EventHandler
    fun onPlayerLocaleChange(e: PlayerLocaleChangeEvent) { call(e) }

    @EventHandler
    fun onPlayerLogin(e: PlayerLoginEvent) { call(e) }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) { call(e) }

    @EventHandler
    fun onPlayerPickupArrow(e: PlayerPickupArrowEvent) { call(e) }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) { call(e) }

    @EventHandler
    fun onPlayerResourcePackStatus(e: PlayerResourcePackStatusEvent) { call(e) }

    @EventHandler
    fun onPlayerRespawn(e: PlayerRespawnEvent) { call(e) }

    @EventHandler
    fun onPlayerShearEntity(e: PlayerShearEntityEvent) { call(e) }

    @EventHandler
    fun onPlayerSpawnLocation(e: PlayerSpawnLocationEvent) { call(e) }

    @EventHandler
    fun onPlayerStatisticIncrement(e: PlayerStatisticIncrementEvent) { call(e) }

    @EventHandler
    fun onPlayerSwapHandItems(e: PlayerSwapHandItemsEvent) { call(e) }

    @EventHandler
    fun onPlayerToggleFlight(e: PlayerToggleFlightEvent) { call(e) }

    @EventHandler
    fun onPlayerToggleSneak(e: PlayerToggleSneakEvent) { call(e) }

    @EventHandler
    fun onPlayerToggleSprint(e: PlayerToggleSprintEvent) { call(e) }

    @EventHandler
    fun onPlayerVelocity(e: PlayerVelocityEvent) { call(e) }

    /*
    @EventHandler
    fun onPlayer(e: Player) { call(e) }
    */
    
    //endregion
    
    //region BlockEvent
    
    @EventHandler
    fun onBlockBurn(e: BlockBurnEvent) { call(e) }

    @EventHandler
    fun onBlockCanBuild(e: BlockCanBuildEvent) { call(e) }

    @EventHandler
    fun onBlockDamage(e: BlockDamageEvent) { call(e) }

    @EventHandler
    fun onBlockDispense(e: BlockDispenseEvent) { call(e) }

    @EventHandler
    fun onBlockExp(e: BlockExpEvent) { call(e) }
    
    @EventHandler
    fun onBlockExplode(e: BlockExplodeEvent) { call(e) }

    @EventHandler
    fun onBlockFade(e: BlockFadeEvent) { call(e) }

    @EventHandler
    fun onBlockFromTo(e: BlockFromToEvent) { call(e) }

    @EventHandler
    fun onBlockGrow(e: BlockGrowEvent) { call(e) }

    @EventHandler
    fun onBlockIgnite(e: BlockIgniteEvent) { call(e) }

    @EventHandler
    fun onBlockPhysics(e: BlockPhysicsEvent) { call(e) }

    @EventHandler
    fun onBlockPistonExtend(e: BlockPistonExtendEvent) { call(e) }

    @EventHandler
    fun onBlockPistonRetract(e: BlockPistonRetractEvent) { call(e) }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) { call(e) }

    @EventHandler
    fun onBlockRedstone(e: BlockRedstoneEvent) { call(e) }

    @EventHandler
    fun onBrew(e: BrewEvent) { call(e) }

    @EventHandler
    fun onBrewingStandFuel(e: BrewingStandFuelEvent) { call(e) }

    @EventHandler
    fun onCauldronLevelChange(e: CauldronLevelChangeEvent) { call(e) }

    @EventHandler
    fun onFurnaceBurn(e: FurnaceBurnEvent) { call(e) }

    @EventHandler
    fun onLeavesDecay(e: LeavesDecayEvent) { call(e) }

    @EventHandler
    fun onNotePlay(e: NotePlayEvent) { call(e) }

    @EventHandler
    fun onSignChange(e: SignChangeEvent) { call(e) }

    /*
    @EventHandler
    fun onBlock(e: Block) { call(e) }
    */
    
    //endregion
    
}

object SEventSubscriberListener2 : SEventSubscriberListener() {

    //region EntityEvent

    @EventHandler
    fun onAreaEffectCloudApply(e: AreaEffectCloudApplyEvent) {
        call(e)
    }

    @EventHandler
    fun onCreeperPower(e: CreeperPowerEvent) {
        call(e)
    }

    @EventHandler
    fun onEnderDragonChangePhase(e: EnderDragonChangePhaseEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityAirChange(e: EntityAirChangeEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityBreed(e: EntityBreedEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityChangeBlock(e: EntityChangeBlockEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityCombust(e: EntityCombustEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityDeath(e: EntityDeathEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityDismount(e: EntityDismountEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityExplode(e: EntityExplodeEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityInteract(e: EntityInteractEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityMount(e: EntityMountEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityPickupItem(e: EntityPickupItemEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityPortalEnter(e: EntityPortalEnterEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityRegainHealth(e: EntityRegainHealthEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityResurrect(e: EntityResurrectEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityShootBow(e: EntityShootBowEvent) {
        call(e)
    }

    @EventHandler
    fun onEntitySpawn(e: EntitySpawnEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityTame(e: EntityTameEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityTarget(e: EntityTargetEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityTeleport(e: EntityTeleportEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityToggleGlide(e: EntityToggleGlideEvent) {
        call(e)
    }

    @EventHandler
    fun onEntityUnleash(e: EntityUnleashEvent) {
        call(e)
    }

    @EventHandler
    fun onExplosionPrime(e: ExplosionPrimeEvent) {
        call(e)
    }

    @EventHandler
    fun onFireworkExplode(e: FireworkExplodeEvent) {
        call(e)
    }

    @EventHandler
    fun onFoodLevelChange(e: FoodLevelChangeEvent) {
        call(e)
    }

    @EventHandler
    fun onHorseJump(e: HorseJumpEvent) {
        call(e)
    }

    @EventHandler
    fun onItemDespawn(e: ItemDespawnEvent) {
        call(e)
    }

    @EventHandler
    fun onItemMerge(e: ItemMergeEvent) {
        call(e)
    }

    @EventHandler
    fun onProjectileHit(e: ProjectileHitEvent) {
        call(e)
    }

    @EventHandler
    fun onSheepDyeWool(e: SheepDyeWoolEvent) {
        call(e)
    }

    @EventHandler
    fun onSheepRegrowWool(e: SheepRegrowWoolEvent) {
        call(e)
    }

    @EventHandler
    fun onSlimeSplit(e: SlimeSplitEvent) {
        call(e)
    }

    @EventHandler
    fun onVillagerAcquireTrade(e: VillagerAcquireTradeEvent) {
        call(e)
    }

    @EventHandler
    fun onVillagerReplenishTrade(e: VillagerReplenishTradeEvent) {
        call(e)
    }

    /*
    @EventHandler
    fun on(e: ) { call(e) }
    */

    //endregion

    //region InventoryEvent

    @EventHandler
    fun onEnchantItem(e: EnchantItemEvent) {
        call(e)
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        call(e)
    }

    @EventHandler
    fun onCraftItem(e: CraftItemEvent) {
        call(e)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        call(e)
    }

    @EventHandler
    fun onInventoryCreative(e: InventoryCreativeEvent) {
        call(e)
    }

    @EventHandler
    fun onInventoryDrag(e: InventoryDragEvent) {
        call(e)
    }

    @EventHandler
    fun onInventoryOpen(e: InventoryOpenEvent) {
        call(e)
    }

    @EventHandler
    fun onPrepareAnvil(e: PrepareAnvilEvent) {
        call(e)
    }

    @EventHandler
    fun onPrepareItemCraft(e: PrepareItemCraftEvent) {
        call(e)
    }

    //endregion

    //region HangingEvent

    @EventHandler
    fun onHangingBreak(e: HangingBreakEvent) {
        call(e)
    }

    @EventHandler
    fun onHangingPlace(e: HangingPlaceEvent) {
        call(e)
    }

    //endregion

    //region InventoryMoveItemEvent

    @EventHandler
    fun onInventoryMoveItem(e: InventoryMoveItemEvent) {
        call(e)
    }

    //endregion

    //region InventoryPickupItemEvent

    @EventHandler
    fun onInventoryPickupItem(e: InventoryPickupItemEvent) {
        call(e)
    }

    //endregion
}

object SEventSubscriberListener3 : SEventSubscriberListener() {
    
    //region AsyncPlayerPreLoginEvent

    @EventHandler
    fun onAsyncPlayerPreLogin(e: AsyncPlayerPreLoginEvent) {
        call(e)
    }

    //endregion

    //region PlayerLeashEntityEvent

    @EventHandler
    fun onPlayerLeashEntity(e: PlayerLeashEntityEvent) {
        call(e)
    }

    //endregion

    //region ServerEvent

    @EventHandler
    fun onBroadcastMessage(e: BroadcastMessageEvent) {
        call(e)
    }

    @EventHandler
    fun onMapInitialize(e: MapInitializeEvent) {
        call(e)
    }

    @EventHandler
    fun onPluginEnable(e: PluginEnableEvent) {
        call(e)
    }

    @EventHandler
    fun onPluginDisable(e: PluginDisableEvent) {
        call(e)
    }

    @EventHandler
    fun onServerCommand(e: ServerCommandEvent) {
        call(e)
    }

    @EventHandler
    fun onServerListPing(e: ServerListPingEvent) {
        call(e)
    }

    @EventHandler
    fun onServiceRegister(e: ServiceRegisterEvent) {
        call(e)
    }

    @EventHandler
    fun onServiceUnregister(e: ServiceUnregisterEvent) {
        call(e)
    }

    //endregion

    //region TabCompleteEvent

    @EventHandler
    fun onTabComplete(e: TabCompleteEvent) {
        call(e)
    }

    //endregion

    //region VehicleEvent

    @EventHandler
    fun onVehicleBlockCollision(e: VehicleBlockCollisionEvent) {
        call(e)
    }

    @EventHandler
    fun onVehicleEntityCollision(e: VehicleEntityCollisionEvent) {
        call(e)
    }

    @EventHandler
    fun onVehicleCreate(e: VehicleCreateEvent) {
        call(e)
    }

    @EventHandler
    fun onVehicleDamage(e: VehicleDamageEvent) {
        call(e)
    }

    @EventHandler
    fun onVehicleDestroy(e: VehicleDestroyEvent) {
        call(e)
    }

    @EventHandler
    fun onVehicleEnter(e: VehicleEnterEvent) {
        call(e)
    }

    @EventHandler
    fun onVehicleExit(e: VehicleExitEvent) {
        call(e)
    }

    @EventHandler
    fun onVehicleMove(e: VehicleMoveEvent) {
        call(e)
    }

    @EventHandler
    fun onVehicleUpdate(e: VehicleUpdateEvent) {
        call(e)
    }

    //endregion

    //region WeatherEvent

    @EventHandler
    fun onLightningStrike(e: LightningStrikeEvent) {
        call(e)
    }

    @EventHandler
    fun onThunderChange(e: ThunderChangeEvent) {
        call(e)
    }

    @EventHandler
    fun onWeatherChange(e: WeatherChangeEvent) {
        call(e)
    }

    //endregion

    //region WorldEvent

    @EventHandler
    fun onChunkLoad(e: ChunkLoadEvent) {
        call(e)
    }

    @EventHandler
    fun onChunkPopulate(e: ChunkPopulateEvent) {
        call(e)
    }

    @EventHandler
    fun onChunkUnload(e: ChunkUnloadEvent) {
        call(e)
    }

    @EventHandler
    fun onPortalCreate(e: PortalCreateEvent) {
        call(e)
    }

    @EventHandler
    fun onSpawnChange(e: SpawnChangeEvent) {
        call(e)
    }

    @EventHandler
    fun onStructureGrow(e: StructureGrowEvent) {
        call(e)
    }

    @EventHandler
    fun onWorldInit(e: WorldInitEvent) {
        call(e)
    }

    @EventHandler
    fun onWorldLoad(e: WorldLoadEvent) {
        call(e)
    }

    @EventHandler
    fun onWorldSave(e: WorldSaveEvent) {
        call(e)
    }

    @EventHandler
    fun onWorldUnload(e: WorldUnloadEvent) {
        call(e)
    }

    //endregion
}

object SEventSubscriberListener4 : SEventSubscriberListener() {
    
    //region SMachineEvent
    
    @EventHandler
    fun onSMachineAdd(e: SMachineAddEvent) {
        call(e)
    }
    
    @EventHandler
    fun onSMachineRemove(e: SMachineRemoveEvent) {
        call(e)
    }
    
    //endregion
    
    //region SMenuEvent
    
    @EventHandler
    fun onSMenuClick(e: SMenuClickEvent) {
        call(e)
    }
    
    @EventHandler
    fun onSMenuOpen(e: SMenuOpenEvent) {
        call(e)
    }
    
    //endregion
}