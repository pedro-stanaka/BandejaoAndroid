#!/usr/bin/env ruby

require 'mini_magick'
require 'fileutils'

densities = {
    'xxxhdpi' => 4.0,
    'xxhdpi'  => 3.0,
    'xhdpi'   => 2.0,
    'hdpi'    => 1.5,
    'mdpi'    => 1.0,
    'ldpi'    => 0.75 }

launcher_baseline = 48
global = (ARGV[0] == 'global')

densities.each do |name, factor|
  launcher_image = MiniMagick::Image.open(ARGV[1])
  resolution = factor * launcher_baseline
  launcher_image.resize "#{resolution}x#{resolution}"
  launcher_image.format 'png'
  path = "launcher/res/mipmap-#{name}/ic_launcher.png"
  dir = File.dirname(path)
  FileUtils.mkdir_p(dir) unless File.directory? dir
  launcher_image.write "#{dir}/ic_launcher.png"

  resource_dir = global ? 'main' : "overlay/#{ARGV[0]}/"
  FileUtils.cp_r 'launcher/res/', "../src/#{resource_dir}"

  FileUtils.rm_rf 'launcher'
end


