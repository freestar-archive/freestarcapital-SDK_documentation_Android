require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = package['name']
  s.version      = package['version']
  s.summary      = package['description']
  s.license      = package['license']

  s.authors      = package['author']
  s.homepage     = package['homepage']
  s.platforms    = { :ios => "10.0" }

  s.source       = { :git => "https://gitlab.com/freestar/freestar-react-native-plugin.git", :tag => "release_v#{s.version}" }
  s.source_files  = "ios/**/*.{h,m}"

  s.dependency 'FreestarAds', '~> 3.4'
  s.dependency 'React'
end
